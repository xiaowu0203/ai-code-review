package com.ai.codereview.mapper;

import com.ai.codereview.entity.PullRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * PullRequest Mapper接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Mapper
public interface PullRequestMapper extends BaseMapper<PullRequest> {
}