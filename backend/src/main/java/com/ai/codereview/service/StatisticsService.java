package com.ai.codereview.service;

import com.ai.codereview.dto.StatisticsDTO;

/**
 * 统计数据服务接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
public interface StatisticsService {

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    StatisticsDTO getStatistics();
}