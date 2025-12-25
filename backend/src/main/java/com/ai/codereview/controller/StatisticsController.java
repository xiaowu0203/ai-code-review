package com.ai.codereview.controller;

import com.ai.codereview.dto.CommonResult;
import com.ai.codereview.dto.StatisticsDTO;
import com.ai.codereview.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计数据控制器
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    @GetMapping
    public CommonResult<StatisticsDTO> getStatistics() {
        try {
            StatisticsDTO statistics = statisticsService.getStatistics();
            return CommonResult.success(statistics);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return CommonResult.failed("获取统计数据失败: " + e.getMessage());
        }
    }
}