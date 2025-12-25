package com.ai.codereview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 代码审核记录实体类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("code_review")
public class CodeReview {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * PR ID
     */
    @TableField("pr_id")
    private Long prId;

    /**
     * 审核类型：auto, manual
     */
    @TableField("review_type")
    private String reviewType;

    /**
     * AI模型
     */
    @TableField("ai_model")
    private String aiModel;

    /**
     * 代码差异内容
     */
    @TableField("diff_content")
    private String diffContent;

    /**
     * 审核结果JSON
     */
    @TableField("review_result")
    private String reviewResult;

    /**
     * 状态：processing, completed, failed
     */
    @TableField("status")
    private String status;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 触发时间
     */
    @TableField("trigger_time")
    private LocalDateTime triggerTime;

    /**
     * 完成时间
     */
    @TableField("complete_time")
    private LocalDateTime completeTime;

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

    /**
     * 关联的PR信息（非数据库字段）
     */
    @TableField(exist = false)
    private PullRequest pullRequest;
}