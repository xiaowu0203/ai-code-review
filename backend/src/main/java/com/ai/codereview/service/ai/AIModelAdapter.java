package com.ai.codereview.service.ai;

import com.ai.codereview.entity.AIModelConfig;

/**
 * AI模型适配器接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
public interface AIModelAdapter {

    /**
     * 审核代码
     *
     * @param diff 代码差异内容
     * @param modelConfig 模型配置
     * @return 审核结果
     */
    String reviewCode(String diff, AIModelConfig modelConfig);

    /**
     * 获取模型代码
     *
     * @return 模型代码
     */
    String getModelCode();

    /**
     * 测试连接
     *
     * @param modelConfig 模型配置
     * @return 是否测试成功
     */
    boolean testConnection(AIModelConfig modelConfig);
}