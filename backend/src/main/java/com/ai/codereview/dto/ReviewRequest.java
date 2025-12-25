package com.ai.codereview.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 代码审核请求DTO
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
public class ReviewRequest {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * PR ID
     */
    @NotNull(message = "PR ID不能为空")
    private Long prId;

    /**
     * AI模型（可选，默认使用项目配置的模型）
     */
    private String aiModel;

    /**
     * 审核类型：auto, manual
     */
    private String reviewType = "manual";
}