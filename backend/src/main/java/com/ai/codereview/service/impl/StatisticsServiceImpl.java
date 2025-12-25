package com.ai.codereview.service.impl;

import com.ai.codereview.dto.StatisticsDTO;
import com.ai.codereview.entity.CodeReview;
import com.ai.codereview.mapper.CodeReviewMapper;
import com.ai.codereview.mapper.ProjectMapper;
import com.ai.codereview.service.StatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计数据服务实现类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final CodeReviewMapper codeReviewMapper;
    private final ProjectMapper projectMapper;

    public StatisticsServiceImpl(CodeReviewMapper codeReviewMapper, ProjectMapper projectMapper) {
        this.codeReviewMapper = codeReviewMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public StatisticsDTO getStatistics() {
        StatisticsDTO statistics = new StatisticsDTO();

        // 1. 获取总览统计数据
        getOverviewStats(statistics);

        // 2. 获取最近7天的审核趋势
        getTrendData(statistics);

        // 3. 获取AI模型使用分布
        getModelUsage(statistics);

        // 4. 获取项目审核排行
        getProjectRanking(statistics);

        return statistics;
    }

    /**
     * 获取总览统计数据
     */
    private void getOverviewStats(StatisticsDTO statistics) {
        QueryWrapper<CodeReview> queryWrapper = new QueryWrapper<>();

        // 总审核次数
        statistics.setTotalReviews(Long.valueOf(codeReviewMapper.selectCount(queryWrapper)));

        // 已完成审核
        queryWrapper.eq("status", "completed");
        statistics.setCompletedReviews(Long.valueOf(codeReviewMapper.selectCount(queryWrapper)));

        // 处理中审核
        queryWrapper.clear();
        queryWrapper.eq("status", "processing");
        statistics.setProcessingReviews(Long.valueOf(codeReviewMapper.selectCount(queryWrapper)));

        // 失败审核
        queryWrapper.clear();
        queryWrapper.eq("status", "failed");
        statistics.setFailedReviews(Long.valueOf(codeReviewMapper.selectCount(queryWrapper)));
    }

    /**
     * 获取最近7天的审核趋势
     */
    private void getTrendData(StatisticsDTO statistics) {
        List<StatisticsDTO.DailyCountDTO> trendData = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime startTime = date.atStartOfDay();
            LocalDateTime endTime = date.plusDays(1).atStartOfDay();

            StatisticsDTO.DailyCountDTO dailyCount = new StatisticsDTO.DailyCountDTO();
            dailyCount.setDate(date.format(formatter));

            // 总数
            QueryWrapper<CodeReview> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("trigger_time", startTime, endTime);
            Long totalCount = Long.valueOf(codeReviewMapper.selectCount(queryWrapper));
            dailyCount.setTotalCount(totalCount != null ? totalCount : 0);

            // 成功数
            queryWrapper.eq("status", "completed");
            Long successCount = Long.valueOf(codeReviewMapper.selectCount(queryWrapper));
            dailyCount.setSuccessCount(successCount != null ? successCount : 0);

            // 失败数
            queryWrapper.clear();
            queryWrapper.eq("status", "failed")
                       .between("trigger_time", startTime, endTime);
            Long failedCount = Long.valueOf(codeReviewMapper.selectCount(queryWrapper));
            dailyCount.setFailedCount(failedCount != null ? failedCount : 0);

            trendData.add(dailyCount);
        }

        statistics.setTrendData(trendData);
    }

    /**
     * 获取AI模型使用分布
     */
    private void getModelUsage(StatisticsDTO statistics) {
        // 查询各模型的使用次数
        QueryWrapper<CodeReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ai_model", "count(*) as count")
                   .groupBy("ai_model");

        List<Map<String, Object>> modelCounts = codeReviewMapper.selectMaps(queryWrapper);
        List<StatisticsDTO.ModelUsageDTO> modelUsage = new ArrayList<>();

        Map<String, String> modelNames = new HashMap<>();
        modelNames.put("qianwen", "千问");
        modelNames.put("glm", "GLM");
        modelNames.put("deepseek", "DeepSeek");

        for (Map<String, Object> count : modelCounts) {
            String modelCode = (String) count.get("ai_model");
            StatisticsDTO.ModelUsageDTO usage = new StatisticsDTO.ModelUsageDTO();
            usage.setModelCode(modelCode);
            usage.setModelName(modelNames.getOrDefault(modelCode, modelCode));
            usage.setCount(((Number) count.get("count")).longValue());
            modelUsage.add(usage);
        }

        statistics.setModelUsage(modelUsage);
    }

    /**
     * 获取项目审核排行
     */
    private void getProjectRanking(StatisticsDTO statistics) {
        try {
            // 使用自定义SQL查询
            List<Map<String, Object>> projectCounts = codeReviewMapper.getProjectRanking();
            List<StatisticsDTO.ProjectRankingDTO> projectRanking = new ArrayList<>();

            for (Map<String, Object> count : projectCounts) {
                StatisticsDTO.ProjectRankingDTO ranking = new StatisticsDTO.ProjectRankingDTO();
                ranking.setProjectId(((Number) count.get("project_id")).longValue());
                ranking.setProjectName((String) count.get("project_name"));
                ranking.setReviewCount(((Number) count.get("review_count")).longValue());
                projectRanking.add(ranking);
            }

            statistics.setProjectRanking(projectRanking);
        } catch (Exception e) {
            log.error("获取项目审核排行失败", e);
            // 如果查询失败，返回空列表
            statistics.setProjectRanking(new ArrayList<>());
        }
    }
}