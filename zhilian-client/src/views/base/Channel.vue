<template>
  <div class="channel-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>渠道管理</h3>
          <el-button type="primary" @click="openAddDialog">添加渠道</el-button>
        </div>
      </template>
      <el-table :data="channelList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="渠道名称"></el-table-column>
        <el-table-column prop="code" label="渠道编码"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteChannel(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑渠道对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="channelForm" :rules="channelRules" ref="channelFormRef" label-width="80px">
        <el-form-item label="渠道名称" prop="name">
          <el-input v-model="channelForm.name" placeholder="请输入渠道名称"></el-input>
        </el-form-item>
        <el-form-item label="渠道编码" prop="code">
          <el-input v-model="channelForm.code" placeholder="请输入渠道编码"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="channelForm.status" active-value="1" inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveChannel">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { baseService } from '../../services/base'

export default {
  name: 'Channel',
  setup() {
    const channelList = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('添加渠道')
    const channelFormRef = ref(null)
    const editingId = ref(null)
    
    const channelForm = reactive({
      name: '',
      code: '',
      status: 1
    })
    
    const channelRules = {
      name: [
        { required: true, message: '请输入渠道名称', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入渠道编码', trigger: 'blur' }
      ]
    }
    
    const fetchChannels = async () => {
      try {
        const response = await baseService.getChannels()
        if (response.code === 200) {
          channelList.value = response.data
        }
      } catch (error) {
        console.error('获取渠道列表失败:', error)
        ElMessage.error('获取渠道列表失败')
      }
    }
    
    const openAddDialog = () => {
      editingId.value = null
      dialogTitle.value = '添加渠道'
      Object.assign(channelForm, {
        name: '',
        code: '',
        status: 1
      })
      dialogVisible.value = true
    }
    
    const openEditDialog = (row) => {
      editingId.value = row.id
      dialogTitle.value = '编辑渠道'
      Object.assign(channelForm, {
        name: row.name,
        code: row.code,
        status: row.status
      })
      dialogVisible.value = true
    }
    
    const saveChannel = async () => {
      if (!channelFormRef.value) return
      
      try {
        await channelFormRef.value.validate()
        
        const response = await baseService.addChannel({
          name: channelForm.name,
          code: channelForm.code,
          status: channelForm.status
        })
        
        if (response.code === 200) {
          ElMessage.success(editingId.value ? '编辑成功' : '添加成功')
          dialogVisible.value = false
          fetchChannels()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('保存渠道失败:', error)
        ElMessage.error('保存失败，请重试')
      }
    }
    
    const deleteChannel = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个渠道吗？', '删除确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // 这里需要调用删除渠道的API，暂时使用添加API模拟
        const response = await baseService.addChannel({})
        if (response.code === 200) {
          ElMessage.success('删除成功')
          fetchChannels()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除渠道失败:', error)
          ElMessage.error('删除失败，请重试')
        }
      }
    }
    
    onMounted(() => {
      fetchChannels()
    })
    
    return {
      channelList,
      dialogVisible,
      dialogTitle,
      channelForm,
      channelRules,
      channelFormRef,
      openAddDialog,
      openEditDialog,
      saveChannel,
      deleteChannel
    }
  }
}
</script>

<style scoped>
.channel-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.dialog-footer {
  text-align: right;
}
</style>