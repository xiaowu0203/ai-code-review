package com.ai.codereview.controller;

import com.ai.codereview.dto.CommonResult;
import com.ai.codereview.dto.PageResult;
import com.ai.codereview.dto.ProjectDTO;
import com.ai.codereview.entity.Project;
import com.ai.codereview.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 项目管理控制器
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/projects")
@Validated
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 获取项目列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 项目列表
     */
    @GetMapping
    public CommonResult<PageResult<Project>> getProjects(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        try {
            PageResult<Project> result = projectService.getProjects(page, size);
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取项目列表失败", e);
            return CommonResult.failed("获取项目列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目
     *
     * @param id 项目ID
     * @return 项目信息
     */
    @GetMapping("/{id}")
    public CommonResult<Project> getProject(@PathVariable @NotNull Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return CommonResult.success(project);
        } catch (Exception e) {
            log.error("获取项目失败，ID: {}", id, e);
            return CommonResult.failed("获取项目失败: " + e.getMessage());
        }
    }

    /**
     * 创建项目
     *
     * @param projectDTO 项目DTO
     * @return 创建的项目
     */
    @PostMapping
    public CommonResult<Project> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        try {
            Project project = projectService.createProject(projectDTO);
            return CommonResult.success("项目创建成功", project);
        } catch (Exception e) {
            log.error("创建项目失败", e);
            return CommonResult.failed("创建项目失败: " + e.getMessage());
        }
    }

    /**
     * 更新项目
     *
     * @param id 项目ID
     * @param projectDTO 项目DTO
     * @return 更新后的项目
     */
    @PutMapping("/{id}")
    public CommonResult<Project> updateProject(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid ProjectDTO projectDTO) {
        try {
            Project project = projectService.updateProject(id, projectDTO);
            return CommonResult.success("项目更新成功", project);
        } catch (Exception e) {
            log.error("更新项目失败，ID: {}", id, e);
            return CommonResult.failed("更新项目失败: " + e.getMessage());
        }
    }

    /**
     * 删除项目
     *
     * @param id 项目ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public CommonResult<String> deleteProject(@PathVariable @NotNull Long id) {
        try {
            projectService.deleteProject(id);
            return CommonResult.success("项目删除成功");
        } catch (Exception e) {
            log.error("删除项目失败，ID: {}", id, e);
            return CommonResult.failed("删除项目失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有启用的项目
     *
     * @return 项目列表
     */
    @GetMapping("/active")
    public CommonResult<?> getActiveProjects() {
        try {
            return CommonResult.success(projectService.getActiveProjects());
        } catch (Exception e) {
            log.error("获取启用项目列表失败", e);
            return CommonResult.failed("获取启用项目列表失败: " + e.getMessage());
        }
    }
}