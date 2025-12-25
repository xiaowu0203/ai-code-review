package com.ai.codereview.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 项目DTO
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
public class ProjectDTO {

    /**
     * 项目ID
     */
    private Long id;

    /**
     * Gitee项目ID
     */
    @NotNull(message = "Gitee项目ID不能为空")
    private Long giteeProjectId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目URL
     */
    @NotBlank(message = "项目URL不能为空")
    private String projectUrl;

    /**
     * Webhook验证Token
     */
    private String webhookToken;

    /**
     * 默认AI模型
     */
    private String aiModel = "qianwen";

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status = 1;

    /**
     * Gitee访问令牌
     */
    @NotBlank(message = "Gitee访问令牌不能为空")
    private String giteeToken;
}