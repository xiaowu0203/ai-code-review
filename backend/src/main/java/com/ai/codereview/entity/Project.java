package com.ai.codereview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目信息实体类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project")
public class Project {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Gitee项目ID
     */
    @TableField("gitee_project_id")
    private Long giteeProjectId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目URL
     */
    @TableField("project_url")
    private String projectUrl;

    /**
     * Webhook验证Token
     */
    @TableField("webhook_token")
    private String webhookToken;

    /**
     * 默认AI模型
     */
    @TableField("ai_model")
    private String aiModel;

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * Gitee访问令牌
     */
    @TableField("gitee_token")
    private String giteeToken;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}