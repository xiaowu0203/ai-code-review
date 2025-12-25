package com.ai.codereview.service.impl;

import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ProjectDTO;
import com.ai.codereview.entity.Project;
import com.ai.codereview.mapper.ProjectMapper;
import com.ai.codereview.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 项目服务实现类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public PageResult<Project> getProjects(int page, int size) {
        try {
            IPage<Project> pageParam = new Page<>(page, size);
            IPage<Project> projectPage = this.page(pageParam);

            return new PageResult<>(
                projectPage.getRecords(),
                projectPage.getTotal(),
                projectPage.getCurrent(),
                projectPage.getSize()
            );
        } catch (Exception e) {
            log.error("获取项目列表失败", e);
            throw new RuntimeException("获取项目列表失败: " + e.getMessage());
        }
    }

    @Override
    public Project getProjectById(Long id) {
        try {
            Project project = this.getById(id);
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            return project;
        } catch (Exception e) {
            log.error("获取项目失败，ID: {}", id, e);
            throw new RuntimeException("获取项目失败: " + e.getMessage());
        }
    }

    @Override
    public Project getProjectByGiteeId(Long giteeProjectId) {
        try {
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("gitee_project_id", giteeProjectId);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error("根据Gitee项目ID获取项目失败，ID: {}", giteeProjectId, e);
            return null;
        }
    }

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        try {
            // 检查是否已存在相同Gitee项目ID的项目
            if (getProjectByGiteeId(projectDTO.getGiteeProjectId()) != null) {
                throw new RuntimeException("该Gitee项目已添加");
            }

            Project project = new Project();
            BeanUtils.copyProperties(projectDTO, project);

            // 生成webhook token
            if (!StringUtils.hasText(project.getWebhookToken())) {
                project.setWebhookToken(generateWebhookToken());
            }

            this.save(project);
            log.info("创建项目成功，项目ID: {}", project.getId());
            return project;
        } catch (Exception e) {
            log.error("创建项目失败", e);
            throw new RuntimeException("创建项目失败: " + e.getMessage());
        }
    }

    @Override
    public Project updateProject(Long id, ProjectDTO projectDTO) {
        try {
            Project existingProject = getProjectById(id);

            // 检查Gitee项目ID是否被其他项目使用
            Project otherProject = getProjectByGiteeId(projectDTO.getGiteeProjectId());
            if (otherProject != null && !otherProject.getId().equals(id)) {
                throw new RuntimeException("该Gitee项目已被其他项目使用");
            }

            BeanUtils.copyProperties(projectDTO, existingProject, "id", "createTime");
            this.updateById(existingProject);

            log.info("更新项目成功，项目ID: {}", id);
            return existingProject;
        } catch (Exception e) {
            log.error("更新项目失败，ID: {}", id, e);
            throw new RuntimeException("更新项目失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteProject(Long id) {
        try {
            if (!this.removeById(id)) {
                throw new RuntimeException("项目不存在");
            }
            log.info("删除项目成功，项目ID: {}", id);
        } catch (Exception e) {
            log.error("删除项目失败，ID: {}", id, e);
            throw new RuntimeException("删除项目失败: " + e.getMessage());
        }
    }

    @Override
    public List<Project> getActiveProjects() {
        try {
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            return this.list(queryWrapper);
        } catch (Exception e) {
            log.error("获取启用项目列表失败", e);
            throw new RuntimeException("获取启用项目列表失败: " + e.getMessage());
        }
    }

    /**
     * 生成Webhook Token
     */
    private String generateWebhookToken() {
        return "ai_review_" + System.currentTimeMillis();
    }
}