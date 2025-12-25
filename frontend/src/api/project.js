import request from './request'

export default {
  // 获取项目列表
  getProjects(params) {
    return request({
      url: '/projects',
      method: 'get',
      params
    })
  },

  // 根据ID获取项目
  getProject(id) {
    return request({
      url: `/projects/${id}`,
      method: 'get'
    })
  },

  // 创建项目
  createProject(data) {
    return request({
      url: '/projects',
      method: 'post',
      data
    })
  },

  // 更新项目
  updateProject(id, data) {
    return request({
      url: `/projects/${id}`,
      method: 'put',
      data
    })
  },

  // 删除项目
  deleteProject(id) {
    return request({
      url: `/projects/${id}`,
      method: 'delete'
    })
  },

  // 获取启用的项目列表
  getActiveProjects() {
    return request({
      url: '/projects/active',
      method: 'get'
    })
  }
}