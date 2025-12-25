import request from './request'

export default {
  // 获取统计数据
  getStatistics() {
    return request({
      url: '/statistics',
      method: 'get'
    })
  }
}