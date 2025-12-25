<template>
  <div class="pull-requests-container">
    <div class="page-header">
      <h2>PR审核管理</h2>
      <div class="header-actions">
        <el-select
          v-model="selectedProjectId"
          placeholder="请选择项目"
          @change="handleProjectChange"
          style="width: 300px; margin-right: 10px"
          no-data-text="暂无项目，请先添加项目"
        >
          <el-option
            v-for="project in projects"
            :key="project.id"
            :label="project.projectName"
            :value="project.id"
          />
        </el-select>
        <el-button type="primary" @click="showReviewDialog" :disabled="!selectedProjectId">
          <Icon name="document" />
          手动触发审核
        </el-button>
      </div>
    </div>

    <!-- 审核历史列表 -->
    <el-card>
      <div v-if="selectedProjectId && reviews.length === 0 && !loading" class="empty-state">
        <el-empty description="暂无审核记录，请先创建PR或手动触发审核">
          <el-button type="primary" @click="showReviewDialog">手动触发审核</el-button>
        </el-empty>
      </div>

      <div v-else-if="!selectedProjectId" class="empty-state">
        <el-empty description="请选择项目查看审核记录"></el-empty>
      </div>

      <el-table
        v-else
        v-loading="loading"
        :data="reviews"
        style="width: 100%"
      >
        <el-table-column prop="prId" label="PR ID" width="100" />
        <el-table-column label="项目" width="200">
          <template #default="{ row }">
            {{ getProjectName(row.prId) }}
          </template>
        </el-table-column>
        <el-table-column prop="reviewType" label="审核类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.reviewType === 'auto' ? 'success' : 'info'">
              {{ row.reviewType === 'auto' ? '自动' : '手动' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="aiModel" label="AI模型" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.status)"
              :loading="row.status === 'processing'"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerTime" label="触发时间" width="180" />
        <el-table-column prop="completeTime" label="完成时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="viewReviewResult(row)">查看结果</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 手动触发审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="手动触发审核"
      width="500px"
    >
      <el-form
        ref="reviewFormRef"
        :model="reviewForm"
        :rules="reviewRules"
        label-width="100px"
      >
        <el-form-item label="PR ID" prop="prId">
          <el-input
            v-model.number="reviewForm.prId"
            placeholder="请输入要审核的PR ID"
          />
        </el-form-item>
        <el-form-item label="AI模型" prop="aiModel">
          <el-select v-model="reviewForm.aiModel" placeholder="请选择AI模型">
            <el-option label="千问" value="qianwen" />
            <el-option label="GLM" value="glm" />
            <el-option label="DeepSeek" value="deepseek" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTriggerReview">确定</el-button>
      </template>
    </el-dialog>

    <!-- 审核结果对话框 -->
    <el-dialog
      v-model="resultDialogVisible"
      title="审核结果"
      width="800px"
    >
      <div v-if="reviewResult">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="PR ID">{{ reviewResult.prId }}</el-descriptions-item>
          <el-descriptions-item label="AI模型">{{ reviewResult.aiModel }}</el-descriptions-item>
          <el-descriptions-item label="审核类型">
            <el-tag :type="reviewResult.reviewType === 'auto' ? 'success' : 'info'">
              {{ reviewResult.reviewType === 'auto' ? '自动' : '手动' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(reviewResult.status)">
              {{ getStatusText(reviewResult.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="触发时间" span="2">
            {{ reviewResult.triggerTime }}
          </el-descriptions-item>
          <el-descriptions-item label="完成时间" span="2" v-if="reviewResult.completeTime">
            {{ reviewResult.completeTime }}
          </el-descriptions-item>
        </el-descriptions>

        <div v-if="reviewResult.status === 'failed'" style="margin-top: 20px">
          <el-alert
            title="审核失败"
            type="error"
            :description="reviewResult.errorMessage"
            show-icon
          />
        </div>

        <div v-if="reviewResult.reviewResult" style="margin-top: 20px">
          <h4>审核结果详情</h4>
          <pre style="background-color: #f5f5f5; padding: 15px; border-radius: 4px; white-space: pre-wrap;">{{ reviewResult.reviewResult }}</pre>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Icon from '@/components/Icon.vue'
import projectApi from '@/api/project'
import reviewApi from '@/api/review'

const loading = ref(false)
const reviews = ref([])
const projects = ref([])
const selectedProjectId = ref(null)
const reviewDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const reviewResult = ref(null)
const reviewFormRef = ref(null)

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const reviewForm = reactive({
  projectId: null,
  prId: null,
  aiModel: ''
})

const reviewRules = {
  prId: [
    { required: true, message: '请输入PR ID', trigger: 'blur' },
    { type: 'number', message: 'PR ID必须为数字', trigger: 'blur' }
  ]
}

// 获取项目列表
const getProjects = async () => {
  try {
    const res = await projectApi.getActiveProjects()
    projects.value = res.data

    // 如果有项目且没有选中项目，默认选择第一个
    if (projects.value.length > 0 && !selectedProjectId.value) {
      selectedProjectId.value = projects.value[0].id
      // 自动加载审核历史
      await getReviews()
    }
  } catch (error) {
    console.error('获取项目列表失败:', error)
  }
}

// 获取审核历史
const getReviews = async () => {
  if (!selectedProjectId.value) return

  loading.value = true
  try {
    const res = await reviewApi.getReviewHistory({
      projectId: selectedProjectId.value,
      page: pagination.current,
      size: pagination.size
    })
    reviews.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('获取审核历史失败:', error)
  } finally {
    loading.value = false
  }
}

// 项目改变
const handleProjectChange = () => {
  pagination.current = 1
  getReviews()
}

// 显示审核对话框
const showReviewDialog = () => {
  reviewDialogVisible.value = true
  reviewForm.projectId = selectedProjectId.value
  reviewForm.prId = null
  reviewForm.aiModel = projects.value.find(p => p.id === selectedProjectId.value)?.aiModel || 'qianwen'
}

// 触发审核
const handleTriggerReview = async () => {
  try {
    await reviewFormRef.value.validate()
    await reviewApi.triggerManualReview(reviewForm)
    ElMessage.success('审核已触发，请稍后查看结果')
    reviewDialogVisible.value = false
    getReviews()
  } catch (error) {
    console.error('触发审核失败:', error)
  }
}

// 查看审核结果
const viewReviewResult = async (row) => {
  try {
    const res = await reviewApi.getReviewResult(row.id)
    reviewResult.value = res.data
    resultDialogVisible.value = true
  } catch (error) {
    console.error('获取审核结果失败:', error)
  }
}

// 获取项目名称
const getProjectName = (prId) => {
  // 从选中的项目中获取名称
  const project = projects.value.find(p => p.id === selectedProjectId.value)
  return project ? project.projectName : '-'
}

// 获取状态类型
const getStatusType = (status) => {
  const map = {
    processing: 'warning',
    completed: 'success',
    failed: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    processing: '处理中',
    completed: '已完成',
    failed: '失败'
  }
  return map[status] || status
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.size = val
  pagination.current = 1
  getReviews()
}

// 当前页改变
const handleCurrentChange = (val) => {
  pagination.current = val
  getReviews()
}

onMounted(async () => {
  await getProjects()
})
</script>

<style scoped>
.pull-requests-container {
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

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}
</style>