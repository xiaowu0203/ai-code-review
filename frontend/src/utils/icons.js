// Element Plus Icons 导入
import * as ElementPlusIcons from '@element-plus/icons-vue'

// 检查图标是否存在
export function getIcon(iconName) {
  return ElementPlusIcons[iconName] || null
}

// 常用图标映射
export const iconMap = {
  // 基础图标
  plus: 'Plus',
  edit: 'Edit',
  delete: 'Delete',
  document: 'Document',
  search: 'Search',
  close: 'Close',
  check: 'Check',

  // 菜单图标
  repository: 'Repository',
  operation: 'Operation', // 替代 Merge
  dataAnalysis: 'DataAnalysis',

  // 状态图标
  circleCheck: 'CircleCheck',
  circleClose: 'CircleClose',
  loading: 'Loading',
  warning: 'Warning',
  info: 'Info',

  // 其他
  arrowLeft: 'ArrowLeft',
  arrowRight: 'ArrowRight',
  refresh: 'Refresh',
  download: 'Download',
  upload: 'Upload'
}

// 获取图标组件
export function getIconComponent(iconName) {
  const realIconName = iconMap[iconName] || iconName
  return ElementPlusIcons[realIconName] || ElementPlusIcons['Document']
}