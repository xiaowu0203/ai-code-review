package com.ai.codereview.controller;

import com.ai.codereview.dto.CommonResult;
import com.ai.codereview.service.CodeReviewService;
import com.ai.codereview.service.ProjectService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Webhook控制器
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CodeReviewService codeReviewService;

    @Value("${ai.code-review.gitee.webhook-token:}")
    private String webhookToken;

    /**
     * 处理Gitee Webhook事件
     *
     * @param payload 请求体
     * @param request HTTP请求
     * @return 处理结果
     */
    @PostMapping("/gitee")
    public CommonResult<String> handleGiteeWebhook(
            @RequestBody String payload,
            HttpServletRequest request) {
        try {
            // 验证webhook token
            String token = request.getHeader("X-Gitee-Token");
            if (webhookToken != null && !webhookToken.isEmpty() && !webhookToken.equals(token)) {
                log.warn("Webhook token验证失败");
                return CommonResult.failed(401, "Token验证失败");
            }

            // 解析webhook payload
            JSONObject webhookData = JSON.parseObject(payload);
            String action = webhookData.getString("action");

            // 只处理PR打开和更新事件
            if (!"open".equals(action) && !"update".equals(action)) {
                log.info("忽略Webhook事件: {}", action);
                return CommonResult.success("事件已接收");
            }

            // 获取PR信息
            JSONObject pullRequest = webhookData.getJSONObject("pull_request");
            if (pullRequest == null) {
                log.warn("Webhook payload中缺少PR信息");
                return CommonResult.success("Payload格式不正确");
            }

            Long prId = pullRequest.getLong("id");
            Integer prNumber = pullRequest.getInteger("number");
            String prTitle = pullRequest.getString("title");

            // 获取项目信息
            JSONObject project = webhookData.getJSONObject("project");
            if (project == null) {
                log.warn("Webhook payload中缺少项目信息");
                return CommonResult.success("Payload格式不正确");
            }

            Long giteeProjectId = project.getLong("id");
            String projectName = project.getString("name");

            log.info("收到Webhook事件 - 项目: {} ({}) PR: #{} ({})",
                projectName, giteeProjectId, prNumber, prTitle);

            // 查找项目配置
            com.ai.codereview.entity.Project projectConfig =
                projectService.getProjectByGiteeId(giteeProjectId);

            if (projectConfig == null) {
                log.warn("未找到项目配置，Gitee项目ID: {}", giteeProjectId);
                return CommonResult.success("项目未配置");
            }

            if (projectConfig.getStatus() != 1) {
                log.info("项目已禁用，跳过审核");
                return CommonResult.success("项目已禁用");
            }

            // 检查是否需要触发审核
            if (shouldTriggerReview(pullRequest, action)) {
                // 异步触发代码审核
                codeReviewService.triggerAutoReview(projectConfig.getId(), prId);
                log.info("已触发代码审核，项目ID: {}, PR ID: {}",
                    projectConfig.getId(), prId);
            }

            return CommonResult.success("Webhook处理成功");
        } catch (Exception e) {
            log.error("处理Webhook失败", e);
            return CommonResult.failed("Webhook处理失败: " + e.getMessage());
        }
    }

    /**
     * 判断是否需要触发审核
     */
    private boolean shouldTriggerReview(JSONObject pullRequest, String action) {
        // PR打开时总是触发
        if ("open".equals(action)) {
            return true;
        }

        // PR更新时，检查是否有新的代码变更
        if ("update".equals(action)) {
            // TODO: 可以根据需要添加更精确的判断逻辑
            // 例如：检查是否只更新了标题、描述等非代码内容
            return true;
        }

        return false;
    }
}