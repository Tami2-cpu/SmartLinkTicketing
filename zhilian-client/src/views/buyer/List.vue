<template>
  <div class="buyer-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>购票人管理</h3>
          <el-button type="primary" @click="openAddDialog">添加购票人</el-button>
        </div>
      </template>
      <el-table :data="buyerList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="idCard" label="身份证号"></el-table-column>
        <el-table-column prop="mobile" label="手机号"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteBuyer(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑购票人对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="buyerForm" :rules="buyerRules" ref="buyerFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="buyerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="buyerForm.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="buyerForm.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBuyer">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { buyerService } from '../../services/buyer'

export default {
  name: 'BuyerList',
  setup() {
    const buyerList = ref([])
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const dialogVisible = ref(false)
    const dialogTitle = ref('添加购票人')
    const buyerFormRef = ref(null)
    const editingId = ref(null)
    
    const buyerForm = reactive({
      name: '',
      idCard: '',
      mobile: ''
    })
    
    const buyerRules = {
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
      ],
      mobile: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ]
    }
    
    const fetchBuyers = async () => {
      try {
        const response = await buyerService.list()
        if (response.code === 200) {
          buyerList.value = response.data
          total.value = response.data.length
        }
      } catch (error) {
        console.error('获取购票人列表失败:', error)
        ElMessage.error('获取购票人列表失败')
      }
    }
    
    const openAddDialog = () => {
      editingId.value = null
      dialogTitle.value = '添加购票人'
      Object.assign(buyerForm, {
        name: '',
        idCard: '',
        mobile: ''
      })
      dialogVisible.value = true
    }
    
    const openEditDialog = (row) => {
      editingId.value = row.id
      dialogTitle.value = '编辑购票人'
      Object.assign(buyerForm, {
        name: row.name,
        idCard: row.idCard,
        mobile: row.mobile
      })
      dialogVisible.value = true
    }
    
    const saveBuyer = async () => {
      if (!buyerFormRef.value) return
      
      try {
        await buyerFormRef.value.validate()
        
        let response
        if (editingId.value) {
          // 编辑
          response = await buyerService.update({
            id: editingId.value,
            name: buyerForm.name,
            idCard: buyerForm.idCard,
            mobile: buyerForm.mobile
          })
        } else {
          // 添加
          response = await buyerService.add({
            name: buyerForm.name,
            idCard: buyerForm.idCard,
            mobile: buyerForm.mobile
          })
        }
        
        if (response.code === 200) {
          ElMessage.success(editingId.value ? '编辑成功' : '添加成功')
          dialogVisible.value = false
          fetchBuyers()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('保存购票人失败:', error)
        ElMessage.error('保存失败，请重试')
      }
    }
    
    const deleteBuyer = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个购票人吗？', '删除确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await buyerService.delete(id)
        if (response.code === 200) {
          ElMessage.success('删除成功')
          fetchBuyers()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除购票人失败:', error)
          ElMessage.error('删除失败，请重试')
        }
      }
    }
    
    const handleSizeChange = (size) => {
      pageSize.value = size
      fetchBuyers()
    }
    
    const handleCurrentChange = (current) => {
      currentPage.value = current
      fetchBuyers()
    }
    
    onMounted(() => {
      fetchBuyers()
    })
    
    return {
      buyerList,
      total,
      currentPage,
      pageSize,
      dialogVisible,
      dialogTitle,
      buyerForm,
      buyerRules,
      buyerFormRef,
      openAddDialog,
      openEditDialog,
      saveBuyer,
      deleteBuyer,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.buyer-list-container {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}
</style>