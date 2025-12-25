package com.ai.codereview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Pull Request实体类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pull_request")
public class PullRequest {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * Gitee PR ID
     */
    @TableField("gitee_pr_id")
    private Long giteePrId;

    /**
     * PR编号
     */
    @TableField("pr_number")
    private Integer prNumber;

    /**
     * PR标题
     */
    @TableField("pr_title")
    private String prTitle;

    /**
     * PR URL
     */
    @TableField("pr_url")
    private String prUrl;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 源分支
     */
    @TableField("source_branch")
    private String sourceBranch;

    /**
     * 目标分支
     */
    @TableField("target_branch")
    private String targetBranch;

    /**
     * 状态：open, merged, closed
     */
    @TableField("status")
    private String status;

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