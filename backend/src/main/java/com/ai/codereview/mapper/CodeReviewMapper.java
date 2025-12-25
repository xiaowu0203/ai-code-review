package com.ai.codereview.mapper;

import com.ai.codereview.entity.CodeReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 代码审核Mapper接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Mapper
public interface CodeReviewMapper extends BaseMapper<CodeReview> {

    /**
     * 获取项目审核排行
     *
     * @return 项目审核排行列表
     */
    List<Map<String, Object>> getProjectRanking();

    /**
     * 获取审核历史（带PR信息）
     *
     * @param projectId 项目ID
     * @param prId PR ID
     * @return 审核历史列表
     */
    List<Map<String, Object>> getReviewHistoryWithPR(@Param("projectId") Long projectId, @Param("prId") Long prId);
}