<template>
  <div class="projects-container">
    <div class="page-header">
      <h2>项目管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <Icon name="plus" />
        添加项目
      </el-button>
    </div>

    <!-- 项目列表 -->
    <el-card>
      <el-table
        v-loading="loading"
        :data="projects"
        style="width: 100%"
      >
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="giteeProjectId" label="Gitee项目ID" width="120" />
        <el-table-column prop="projectUrl" label="项目URL" show-overflow-tooltip />
        <el-table-column prop="aiModel" label="AI模型" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">
              <Icon name="edit" />编辑
            </el-button>
            <el-button type="text" @click="handleDelete(row)">
              <Icon name="delete" />删除
            </el-button>
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

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="Gitee项目ID" prop="giteeProjectId">
          <el-input v-model="form.giteeProjectId" placeholder="请输入Gitee项目ID" />
        </el-form-item>
        <el-form-item label="项目URL" prop="projectUrl">
          <el-input v-model="form.projectUrl" placeholder="请输入项目URL" />
        </el-form-item>
        <el-form-item label="Gitee Token" prop="giteeToken">
          <el-input
            v-model="form.giteeToken"
            type="password"
            placeholder="请输入Gitee访问令牌"
            show-password
          />
        </el-form-item>
        <el-form-item label="默认AI模型" prop="aiModel">
          <el-select v-model="form.aiModel" placeholder="请选择AI模型">
            <el-option label="千问" value="qianwen" />
            <el-option label="GLM" value="glm" />
            <el-option label="DeepSeek" value="deepseek" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Icon from '@/components/Icon.vue'
import projectApi from '@/api/project'

const loading = ref(false)
const projects = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加项目')
const formRef = ref(null)

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  projectName: '',
  giteeProjectId: '',
  projectUrl: '',
  giteeToken: '',
  aiModel: 'qianwen',
  status: 1
})

const rules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' }
  ],
  giteeProjectId: [
    { required: true, message: '请输入Gitee项目ID', trigger: 'blur' }
  ],
  projectUrl: [
    { required: true, message: '请输入项目URL', trigger: 'blur' },
    { type: 'url', message: '请输入正确的URL格式', trigger: 'blur' }
  ],
  giteeToken: [
    { required: true, message: '请输入Gitee访问令牌', trigger: 'blur' }
  ]
}

// 获取项目列表
const getProjects = async () => {
  loading.value = true
  try {
    const res = await projectApi.getProjects({
      page: pagination.current,
      size: pagination.size
    })
    projects.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('获取项目列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 显示添加对话框
const showAddDialog = () => {
  dialogTitle.value = '添加项目'
  dialogVisible.value = true
  resetForm()
}

// 编辑项目
const handleEdit = (row) => {
  dialogTitle.value = '编辑项目'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 删除项目
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除项目"${row.projectName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await projectApi.deleteProject(row.id)
      ElMessage.success('删除成功')
      getProjects()
    } catch (error) {
      console.error('删除项目失败:', error)
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (form.id) {
      await projectApi.updateProject(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await projectApi.createProject(form)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    getProjects()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    projectName: '',
    giteeProjectId: '',
    projectUrl: '',
    giteeToken: '',
    aiModel: 'qianwen',
    status: 1
  })
  formRef.value?.clearValidate()
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.size = val
  pagination.current = 1
  getProjects()
}

// 当前页改变
const handleCurrentChange = (val) => {
  pagination.current = val
  getProjects()
}

onMounted(() => {
  getProjects()
})
</script>

<style scoped>
.projects-container {
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

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>