package com.ai.codereview.service;

import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ProjectDTO;
import com.ai.codereview.entity.Project;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author AI Code Review
 * @since 1.0.0
 */
public interface ProjectService {

    /**
     * 获取项目列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 项目列表
     */
    PageResult<Project> getProjects(int page, int size);

    /**
     * 根据ID获取项目
     *
     * @param id 项目ID
     * @return 项目信息
     */
    Project getProjectById(Long id);

    /**
     * 根据Gitee项目ID获取项目
     *
     * @param giteeProjectId Gitee项目ID
     * @return 项目信息
     */
    Project getProjectByGiteeId(Long giteeProjectId);

    /**
     * 创建项目
     *
     * @param projectDTO 项目DTO
     * @return 创建的项目
     */
    Project createProject(ProjectDTO projectDTO);

    /**
     * 更新项目
     *
     * @param id 项目ID
     * @param projectDTO 项目DTO
     * @return 更新后的项目
     */
    Project updateProject(Long id, ProjectDTO projectDTO);

    /**
     * 删除项目
     *
     * @param id 项目ID
     */
    void deleteProject(Long id);

    /**
     * 获取所有启用的项目
     *
     * @return 项目列表
     */
    List<Project> getActiveProjects();
}