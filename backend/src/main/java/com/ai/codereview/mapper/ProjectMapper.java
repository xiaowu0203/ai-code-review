package com.ai.codereview.mapper;

import com.ai.codereview.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目Mapper接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}