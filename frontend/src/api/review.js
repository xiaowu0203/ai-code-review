import request from './request'

export default {
  // 手动触发审核
  triggerManualReview(data) {
    return request({
      url: '/reviews/manual',
      method: 'post',
      data
    })
  },

  // 获取审核历史
  getReviewHistory(params) {
    return request({
      url: '/reviews/history',
      method: 'get',
      params
    })
  },

  // 获取审核结果
  getReviewResult(reviewId) {
    return request({
      url: `/reviews/${reviewId}`,
      method: 'get'
    })
  }
}