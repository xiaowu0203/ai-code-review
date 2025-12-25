<template>
  <div class="statistics-container">
    <div class="page-header">
      <h2>统计分析</h2>
      <el-button type="primary" :loading="loading" @click="loadStats">
        <Icon name="refresh" />
        刷新数据
      </el-button>
    </div>

    <el-row :gutter="20" v-loading="loading">
      <!-- 统计卡片 -->
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #409EFF">
              <Icon name="document" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalReviews }}</div>
              <div class="stat-label">总审核次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #67C23A">
              <Icon name="circleCheck" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completedReviews }}</div>
              <div class="stat-label">已完成审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #E6A23C">
              <Icon name="loading" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.processingReviews }}</div>
              <div class="stat-label">处理中审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #F56C6C">
              <Icon name="circleClose" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.failedReviews }}</div>
              <div class="stat-label">失败审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>审核趋势（最近7天）</span>
            </div>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>AI模型使用分布</span>
            </div>
          </template>
          <div ref="modelChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>项目审核排行</span>
            </div>
          </template>
          <div ref="projectChartRef" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import Icon from '@/components/Icon.vue'
import * as echarts from 'echarts'
import statisticsApi from '@/api/statistics'

const trendChartRef = ref(null)
const modelChartRef = ref(null)
const projectChartRef = ref(null)
let trendChart = null
let modelChart = null
let projectChart = null

const loading = ref(false)
const stats = ref({
  totalReviews: 0,
  completedReviews: 0,
  processingReviews: 0,
  failedReviews: 0
})

// 获取统计数据
const loadStats = async () => {
  loading.value = true
  try {
    const res = await statisticsApi.getStatistics()
    const data = res.data
    console.log('统计数据:', data)

    // 更新统计数据
    stats.value = {
      totalReviews: data.totalReviews || 0,
      completedReviews: data.completedReviews || 0,
      processingReviews: data.processingReviews || 0,
      failedReviews: data.failedReviews || 0
    }

    // 延迟更新图表，确保DOM已经渲染
    setTimeout(() => {
      console.log('开始更新图表...')
      console.log('trendChart存在:', !!trendChart)
      console.log('modelChart存在:', !!modelChart)
      console.log('projectChart存在:', !!projectChart)

      if (trendChart && data.trendData) {
        console.log('更新趋势图:', data.trendData)
        updateTrendChart(data.trendData)
      }
      if (modelChart && data.modelUsage) {
        console.log('更新模型图:', data.modelUsage)
        updateModelChart(data.modelUsage)
      }
      if (projectChart && data.projectRanking) {
        console.log('更新项目图:', data.projectRanking)
        updateProjectChart(data.projectRanking)
      }
    }, 200)
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  } finally {
    loading.value = false
  }
}

// 初始化趋势图
const initTrendChart = () => {
  if (!trendChartRef.value) return

  trendChart = echarts.init(trendChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['审核总数', '成功审核', '失败审核']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: []
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '审核总数',
        type: 'line',
        smooth: true,
        data: []
      },
      {
        name: '成功审核',
        type: 'line',
        smooth: true,
        data: []
      },
      {
        name: '失败审核',
        type: 'line',
        smooth: true,
        data: []
      }
    ]
  }
  trendChart.setOption(option)
}

// 初始化模型分布图
const initModelChart = () => {
  if (!modelChartRef.value) return

  modelChart = echarts.init(modelChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'AI模型',
        type: 'pie',
        radius: '50%',
        data: []
      }
    ]
  }
  modelChart.setOption(option)
}

// 初始化项目排行图
const initProjectChart = () => {
  if (!projectChartRef.value) return

  projectChart = echarts.init(projectChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: []
    },
    series: [
      {
        name: '审核次数',
        type: 'bar',
        data: []
      }
    ]
  }
  projectChart.setOption(option)
}

// 更新趋势图
const updateTrendChart = (trendData) => {
  if (!trendChart) {
    console.log('趋势图未初始化')
    return
  }

  const dates = []
  const totalCounts = []
  const successCounts = []
  const failedCounts = []

  trendData.forEach(item => {
    dates.push(item.date)
    totalCounts.push(item.totalCount)
    successCounts.push(item.successCount)
    failedCounts.push(item.failedCount)
  })

  const option = {
    xAxis: {
      data: dates
    },
    series: [
      { name: '审核总数', data: totalCounts },
      { name: '成功审核', data: successCounts },
      { name: '失败审核', data: failedCounts }
    ]
  }

  console.log('趋势图配置:', option)
  trendChart.setOption(option)
}

// 更新模型分布图
const updateModelChart = (modelUsage) => {
  if (!modelChart) {
    console.log('模型图未初始化')
    return
  }

  const data = modelUsage.map(item => ({
    value: item.count,
    name: item.modelName
  }))

  console.log('模型分布数据:', data)

  modelChart.setOption({
    series: [{
      name: 'AI模型',
      data
    }]
  })
}

// 更新项目排行图
const updateProjectChart = (projectRanking) => {
  if (!projectChart) {
    console.log('项目图未初始化')
    return
  }

  const projectNames = []
  const reviewCounts = []

  projectRanking.forEach(item => {
    projectNames.push(item.projectName)
    reviewCounts.push(item.reviewCount)
  })

  // 反转数组让数据从上到下显示
  const option = {
    yAxis: {
      data: projectNames.reverse()
    },
    series: [{
      data: reviewCounts.reverse()
    }]
  }

  console.log('项目排行配置:', option)
  projectChart.setOption(option)
}

// 窗口大小改变时重绘图表
const handleResize = () => {
  trendChart?.resize()
  modelChart?.resize()
  projectChart?.resize()
}

onMounted(async () => {
  // 先初始化图表
  initTrendChart()
  initModelChart()
  initProjectChart()

  // 然后加载数据
  await loadStats()

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  trendChart?.dispose()
  modelChart?.dispose()
  projectChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.statistics-container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.stat-icon .el-icon {
  font-size: 30px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  font-weight: 500;
}
</style>