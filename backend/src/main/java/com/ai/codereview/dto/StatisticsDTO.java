package com.ai.codereview.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统计数据DTO
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
public class StatisticsDTO {

    /**
     * 总审核次数
     */
    private Long totalReviews;

    /**
     * 已完成审核
     */
    private Long completedReviews;

    /**
     * 处理中审核
     */
    private Long processingReviews;

    /**
     * 失败审核
     */
    private Long failedReviews;

    /**
     * 最近7天的审核趋势
     */
    private List<DailyCountDTO> trendData;

    /**
     * AI模型使用分布
     */
    private List<ModelUsageDTO> modelUsage;

    /**
     * 项目审核排行
     */
    private List<ProjectRankingDTO> projectRanking;

    @Data
    public static class DailyCountDTO {
        private String date;
        private Long totalCount;
        private Long successCount;
        private Long failedCount;
    }

    @Data
    public static class ModelUsageDTO {
        private String modelCode;
        private String modelName;
        private Long count;
    }

    @Data
    public static class ProjectRankingDTO {
        private Long projectId;
        private String projectName;
        private Long reviewCount;
    }
}