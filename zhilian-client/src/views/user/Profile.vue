<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>个人中心</h3>
        </div>
      </template>
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="userForm.mobile" disabled></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :http-request="handleAvatarUpload"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateProfile">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { userService } from '../../services/user'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'Profile',
  components: {
    Plus
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const userFormRef = ref(null)
    
    const userForm = reactive({
      id: '',
      username: '',
      mobile: '',
      email: '',
      nickname: '',
      avatar: ''
    })
    
    const userRules = {
      nickname: [
        { max: 20, message: '昵称长度不超过 20 个字符', trigger: 'blur' }
      ]
    }
    
    const fetchUserInfo = async () => {
      try {
        console.log('开始获取用户信息')
        // 检查localStorage中的token
        const token = localStorage.getItem('token')
        console.log('localStorage中的token:', token)
        
        const userInfo = await userStore.getInfo()
        console.log('获取到的用户信息:', userInfo)
        
        if (userInfo) {
          console.log('更新表单数据')
          Object.assign(userForm, userInfo)
          console.log('更新后的表单数据:', userForm)
        } else {
          // 检查是否有token
          if (!token || token.trim() === '') {
            console.log('未登录，跳转到登录页')
            ElMessage.warning('请先登录')
            router.push('/auth/login')
          } else {
            console.log('有token但获取用户信息失败')
            ElMessage.error('获取用户信息失败')
          }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        ElMessage.error('获取用户信息失败')
      }
    }
    
    const updateProfile = async () => {
      if (!userFormRef.value) return
      
      try {
        await userFormRef.value.validate()
        
        const response = await userService.update({
          id: userForm.id,
          nickname: userForm.nickname,
          avatar: userForm.avatar
        })
        
        if (response.code === 200) {
          ElMessage.success('更新成功')
          userStore.getInfo()
        } else {
          ElMessage.error(response.message || '更新失败')
        }
      } catch (error) {
        console.error('更新个人信息失败:', error)
        ElMessage.error('更新失败，请重试')
      }
    }
    
    const handleAvatarUpload = async (options) => {
      try {
        const response = await userService.uploadAvatar(options.file)
        if (response && response.code === 200 && response.data) {
          userForm.avatar = response.data
          ElMessage.success('头像上传成功')
          options.onSuccess()
        } else {
          ElMessage.error('头像上传失败')
          options.onError()
        }
      } catch (error) {
        console.error('头像上传失败:', error)
        ElMessage.error('头像上传失败，请重试')
        options.onError()
      }
    }
    
    const beforeAvatarUpload = (file) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isJPG) {
        ElMessage.error('只能上传 JPG/PNG 图片')
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB')
      }
      
      return isJPG && isLt2M
    }
    
    onMounted(() => {
      fetchUserInfo()
    })
    
    return {
      userForm,
      userRules,
      userFormRef,
      updateProfile,
      handleAvatarUpload,
      beforeAvatarUpload
    }
  }
}
</script>

<style scoped>
.profile-container {
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

.avatar-uploader {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  border: 2px solid #e0e0e0;
  background-color: #f0f2f5;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: center;
}

.avatar-uploader-icon {
  width: 120px;
  height: 120px;
  font-size: 24px;
  color: #909399;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>