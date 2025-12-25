package com.ai.codereview.service;

import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ReviewRequest;
import com.ai.codereview.entity.CodeReview;

import java.util.concurrent.CompletableFuture;

/**
 * 代码审核服务接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
public interface CodeReviewService {

    /**
     * 手动触发代码审核
     *
     * @param request 审核请求
     * @return 异步任务
     */
    CompletableFuture<Void> triggerManualReview(ReviewRequest request);

    /**
     * 自动触发代码审核（Webhook触发）
     *
     * @param projectId 项目ID
     * @param giteePrId Gitee PR ID
     * @return 异步任务
     */
    CompletableFuture<Void> triggerAutoReview(Long projectId, Long giteePrId);

    /**
     * 获取审核历史
     *
     * @param projectId 项目ID
     * @param prId PR ID（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 审核历史列表
     */
    PageResult<CodeReview> getReviewHistory(Long projectId, Long prId, int page, int size);

    /**
     * 获取审核结果
     *
     * @param reviewId 审核ID
     * @return 审核结果
     */
    CodeReview getReviewResult(Long reviewId);

    /**
     * 处理代码审核
     *
     * @param codeReview 审核记录
     */
    void processCodeReview(CodeReview codeReview);
}