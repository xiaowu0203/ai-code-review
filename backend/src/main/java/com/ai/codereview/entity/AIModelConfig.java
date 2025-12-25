package com.ai.codereview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI模型配置实体类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_model_config")
public class AIModelConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模型代码
     */
    @TableField("model_code")
    private String modelCode;

    /**
     * 模型名称
     */
    @TableField("model_name")
    private String modelName;

    /**
     * API端点
     */
    @TableField("api_endpoint")
    private String apiEndpoint;

    /**
     * API密钥
     */
    @TableField("api_key")
    private String apiKey;

    /**
     * 最大Token数
     */
    @TableField("max_tokens")
    private Integer maxTokens;

    /**
     * 温度参数
     */
    @TableField("temperature")
    private BigDecimal temperature;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Integer isEnabled;

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