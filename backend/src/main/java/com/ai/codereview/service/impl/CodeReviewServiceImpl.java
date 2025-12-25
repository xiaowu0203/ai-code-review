package com.ai.codereview.service.impl;

import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ReviewRequest;
import com.ai.codereview.entity.*;
import com.ai.codereview.mapper.CodeReviewMapper;
import com.ai.codereview.mapper.PullRequestMapper;
import com.ai.codereview.service.*;
import com.ai.codereview.service.ai.AIModelAdapter;
import com.ai.codereview.util.GiteeApiUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 代码审核服务实现类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@Service
public class CodeReviewServiceImpl extends ServiceImpl<CodeReviewMapper, CodeReview> implements CodeReviewService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PullRequestMapper pullRequestMapper;

    @Autowired
    private GiteeApiUtil giteeApiUtil;

    @Autowired
    private List<AIModelAdapter> aiModelAdapters;

    @Autowired
    private CodeReviewMapper codeReviewMapper;

    @Value("${ai.code-review.review.default-model:qianwen}")
    private String defaultModel;

    @Override
    public CompletableFuture<Void> triggerManualReview(ReviewRequest request) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("开始手动触发代码审核，项目ID: {}, PR ID: {}", request.getProjectId(), request.getPrId());

                // 获取项目信息
                Project project = projectService.getProjectById(request.getProjectId());

                // 获取PR信息
                PullRequest pullRequest = pullRequestMapper.selectById(request.getPrId());
                if (pullRequest == null) {
                    throw new RuntimeException("PR不存在");
                }

                // 创建审核记录
                CodeReview codeReview = new CodeReview();
                codeReview.setPrId(request.getPrId());
                codeReview.setReviewType(request.getReviewType());
                codeReview.setAiModel(request.getAiModel() != null ? request.getAiModel() : project.getAiModel());
                codeReview.setStatus("processing");
                codeReview.setTriggerTime(LocalDateTime.now());
                this.save(codeReview);

                // 异步处理审核
                processCodeReview(codeReview);

                log.info("手动触发代码审核任务创建成功，审核ID: {}", codeReview.getId());
            } catch (Exception e) {
                log.error("手动触发代码审核失败", e);
                throw new RuntimeException("触发代码审核失败: " + e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<Void> triggerAutoReview(Long projectId, Long giteePrId) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("开始自动触发代码审核，项目ID: {}, Gitee PR ID: {}", projectId, giteePrId);

                // 获取项目信息
                Project project = projectService.getProjectById(projectId);

                // 查询或创建PR记录
                QueryWrapper<PullRequest> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("project_id", projectId)
                           .eq("gitee_pr_id", giteePrId);
                PullRequest pullRequest = pullRequestMapper.selectOne(queryWrapper);

                if (pullRequest == null) {
                    // 从Gitee获取PR信息
                    pullRequest = createPullRequestFromGitee(project, giteePrId);
                }

                // 创建审核记录
                CodeReview codeReview = new CodeReview();
                codeReview.setPrId(pullRequest.getId());
                codeReview.setReviewType("auto");
                codeReview.setAiModel(project.getAiModel());
                codeReview.setStatus("processing");
                codeReview.setTriggerTime(LocalDateTime.now());
                this.save(codeReview);

                // 异步处理审核
                processCodeReview(codeReview);

                log.info("自动触发代码审核任务创建成功，审核ID: {}", codeReview.getId());
            } catch (Exception e) {
                log.error("自动触发代码审核失败", e);
                throw new RuntimeException("触发代码审核失败: " + e.getMessage());
            }
        });
    }

    @Override
    public PageResult<CodeReview> getReviewHistory(Long projectId, Long prId, int page, int size) {
        try {
            // 直接使用SQL查询获取带PR信息的审核记录
            List<Map<String, Object>> records = codeReviewMapper.getReviewHistoryWithPR(projectId, prId);

            // 转换为CodeReview对象
            List<CodeReview> reviewList = new ArrayList<>();
            for (Map<String, Object> record : records) {
                CodeReview review = new CodeReview();
                review.setId(((Number) record.get("id")).longValue());
                review.setPrId(((Number) record.get("pr_id")).longValue());
                review.setReviewType((String) record.get("review_type"));
                review.setAiModel((String) record.get("ai_model"));
                review.setStatus((String) record.get("status"));
                review.setTriggerTime((LocalDateTime) record.get("trigger_time"));
                review.setCompleteTime((LocalDateTime) record.get("complete_time"));

                // 保存PR信息到review对象
                review.setPullRequest(new PullRequest());
                review.getPullRequest().setProjectName((String) record.get("project_name"));

                reviewList.add(review);
            }

            // 手动分页
            int total = reviewList.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            List<CodeReview> pagedRecords = start < total ? reviewList.subList(start, end) : new ArrayList<>();

            return new PageResult<>(
                pagedRecords,
                (long) total,
                (long) page,
                (long) size
            );
        } catch (Exception e) {
            log.error("获取审核历史失败", e);
            throw new RuntimeException("获取审核历史失败: " + e.getMessage());
        }
    }

    @Override
    public CodeReview getReviewResult(Long reviewId) {
        try {
            CodeReview review = this.getById(reviewId);
            if (review == null) {
                throw new RuntimeException("审核记录不存在");
            }
            return review;
        } catch (Exception e) {
            log.error("获取审核结果失败，ID: {}", reviewId, e);
            throw new RuntimeException("获取审核结果失败: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void processCodeReview(CodeReview codeReview) {
        try {
            log.info("开始处理代码审核，审核ID: {}", codeReview.getId());

            // 获取PR信息
            PullRequest pullRequest = pullRequestMapper.selectById(codeReview.getPrId());
            if (pullRequest == null) {
                throw new RuntimeException("关联的PR不存在");
            }

            // 获取项目信息
            Project project = projectService.getProjectById(pullRequest.getProjectId());

            // 获取代码差异
            String diff = giteeApiUtil.getPullRequestDiff(
                project.getGiteeProjectId(),
                pullRequest.getPrNumber(),
                project.getGiteeToken()
            );

            if (diff == null || diff.isEmpty()) {
                throw new RuntimeException("获取代码差异失败");
            }

            // 保存差异内容
            codeReview.setDiffContent(diff);

            // 获取AI模型适配器
            AIModelAdapter adapter = getAIModelAdapter(codeReview.getAiModel());
            if (adapter == null) {
                throw new RuntimeException("不支持的AI模型: " + codeReview.getAiModel());
            }

            // 构建模型配置
            AIModelConfig modelConfig = buildModelConfig(codeReview.getAiModel());

            // 调用AI模型审核
            String reviewResult = adapter.reviewCode(diff, modelConfig);

            // 保存审核结果
            codeReview.setReviewResult(reviewResult);
            codeReview.setStatus("completed");
            codeReview.setCompleteTime(LocalDateTime.now());
            this.updateById(codeReview);

            // 发送评论到PR
            sendCommentToPR(project, pullRequest, reviewResult);

            log.info("代码审核处理完成，审核ID: {}", codeReview.getId());
        } catch (Exception e) {
            log.error("处理代码审核失败，审核ID: {}", codeReview.getId(), e);
            codeReview.setStatus("failed");
            codeReview.setErrorMessage(e.getMessage());
            this.updateById(codeReview);
        }
    }

    /**
     * 从Gitee创建PR记录
     */
    private PullRequest createPullRequestFromGitee(Project project, Long giteePrId) {
        // TODO: 调用Gitee API获取PR详情
        // 这里简化处理，实际需要调用Gitee API
        PullRequest pullRequest = new PullRequest();
        pullRequest.setProjectId(project.getId());
        pullRequest.setGiteePrId(giteePrId);
        pullRequest.setPrNumber(giteePrId.intValue());
        pullRequest.setPrTitle("PR #" + giteePrId);
        pullRequest.setPrUrl("https://gitee.com/projects/" + project.getGiteeProjectId() + "/pulls/" + giteePrId);
        pullRequest.setAuthor("unknown");
        pullRequest.setSourceBranch("feature");
        pullRequest.setTargetBranch("master");
        pullRequest.setStatus("open");

        pullRequestMapper.insert(pullRequest);
        return pullRequest;
    }

    /**
     * 获取AI模型适配器
     */
    private AIModelAdapter getAIModelAdapter(String modelCode) {
        Map<String, AIModelAdapter> adapterMap = aiModelAdapters.stream()
            .collect(Collectors.toMap(AIModelAdapter::getModelCode, adapter -> adapter));
        return adapterMap.get(modelCode);
    }

    /**
     * 构建模型配置
     */
    private AIModelConfig buildModelConfig(String modelCode) {
        // TODO: 从数据库获取模型配置，这里简化处理
        AIModelConfig config = new AIModelConfig();
        config.setModelCode(modelCode);
        config.setMaxTokens(4096);
        config.setTemperature(java.math.BigDecimal.valueOf(0.7));

        // 从环境变量获取API Key
        switch (modelCode) {
            case "qianwen":
                config.setApiKey(System.getenv("QIANWEN_API_KEY"));
                config.setApiEndpoint(System.getenv("QIANWEN_API_URL"));
                break;
            case "glm":
                config.setApiKey(System.getenv("GLM_API_KEY"));
                config.setApiEndpoint(System.getenv("GLM_API_URL"));
                break;
            case "deepseek":
                config.setApiKey(System.getenv("DEEPSEEK_API_KEY"));
                config.setApiEndpoint(System.getenv("DEEPSEEK_API_URL"));
                break;
        }

        return config;
    }

    /**
     * 发送评论到PR
     */
    private void sendCommentToPR(Project project, PullRequest pullRequest, String reviewResult) {
        try {
            // TODO: 解析审核结果并格式化评论内容
            String comment = "## 🤖 AI代码审核结果\n\n" + reviewResult;

            // 调用Gitee API发送评论
            boolean success = giteeApiUtil.createPRComment(
                project.getGiteeProjectId(),
                pullRequest.getPrNumber(),
                comment,
                project.getGiteeToken()
            );

            if (success) {
                log.info("评论发送成功，PR: {}", pullRequest.getPrNumber());
            } else {
                log.error("评论发送失败，PR: {}", pullRequest.getPrNumber());
            }
        } catch (Exception e) {
            log.error("发送评论到PR失败", e);
        }
    }
}