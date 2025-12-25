package com.ai.codereview.controller;

import com.ai.codereview.dto.CommonResult;
import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ReviewRequest;
import com.ai.codereview.entity.CodeReview;
import com.ai.codereview.service.CodeReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 代码审核控制器
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/reviews")
@Validated
public class CodeReviewController {

    @Autowired
    private CodeReviewService codeReviewService;

    /**
     * 手动触发代码审核
     *
     * @param request 审核请求
     * @return 操作结果
     */
    @PostMapping("/manual")
    public CommonResult<String> triggerManualReview(@RequestBody @Valid ReviewRequest request) {
        try {
            codeReviewService.triggerManualReview(request);
            return CommonResult.success("代码审核已触发，请稍后查看结果");
        } catch (Exception e) {
            log.error("手动触发代码审核失败", e);
            return CommonResult.failed("触发代码审核失败: " + e.getMessage());
        }
    }

    /**
     * 获取审核历史
     *
     * @param projectId 项目ID
     * @param prId PR ID（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 审核历史列表
     */
    @GetMapping("/history")
    public CommonResult<PageResult<CodeReview>> getReviewHistory(
            @RequestParam @NotNull Long projectId,
            @RequestParam(required = false) Long prId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        try {
            PageResult<CodeReview> result = codeReviewService.getReviewHistory(projectId, prId, page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取审核历史失败", e);
            return CommonResult.failed("获取审核历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取审核结果
     *
     * @param reviewId 审核ID
     * @return 审核结果
     */
    @GetMapping("/{reviewId}")
    public CommonResult<CodeReview> getReviewResult(@PathVariable @NotNull Long reviewId) {
        try {
            CodeReview result = codeReviewService.getReviewResult(reviewId);
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取审核结果失败，ID: {}", reviewId, e);
            return CommonResult.failed("获取审核结果失败: " + e.getMessage());
        }
    }
}