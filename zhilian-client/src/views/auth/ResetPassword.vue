<template>
  <div class="reset-password-container">
    <el-card class="reset-password-card">
      <template #header>
        <div class="reset-password-header">
          <h2>智联票务系统</h2>
          <p>重置密码</p>
        </div>
      </template>
      <el-form :model="resetForm" :rules="resetRules" ref="resetFormRef" label-width="120px">
        <el-form-item label="邮箱或手机号" prop="emailOrMobile">
          <el-input v-model="resetForm.emailOrMobile" placeholder="请输入邮箱或手机号"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input v-model="resetForm.code" placeholder="请输入验证码"></el-input>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" :disabled="countdown > 0" @click="sendResetCode">
                {{ countdown > 0 ? `${countdown}秒后重发` : '发送验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="resetForm.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleResetPassword" class="reset-password-btn">重置密码</el-button>
          <el-button @click="goToLogin" class="login-btn">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userService } from '../../services/user'
import { ElMessage } from 'element-plus'

export default {
  name: 'ResetPassword',
  setup() {
    const router = useRouter()
    const resetFormRef = ref(null)
    const countdown = ref(0)
    let countdownTimer = null
    
    const resetForm = reactive({
      emailOrMobile: '',
      code: '',
      newPassword: '',
      confirmPassword: ''
    })
    
    const resetRules = {
      emailOrMobile: [
        { required: true, message: '请输入邮箱或手机号', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== resetForm.newPassword) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }
    
    const sendResetCode = async () => {
      if (!resetForm.emailOrMobile) {
        ElMessage.warning('请输入邮箱或手机号')
        return
      }
      
      try {
        const response = await userService.sendResetCode(resetForm.emailOrMobile)
        if (response.code === 200) {
          ElMessage.success('验证码发送成功')
          startCountdown()
        } else {
          ElMessage.error('验证码发送失败')
        }
      } catch (error) {
        console.error('发送验证码失败:', error)
        ElMessage.error('发送验证码失败，请重试')
      }
    }
    
    const startCountdown = () => {
      countdown.value = 60
      countdownTimer = setInterval(() => {
        if (countdown.value > 0) {
          countdown.value--
        } else {
          clearInterval(countdownTimer)
        }
      }, 1000)
    }
    
    const handleResetPassword = async () => {
      if (!resetFormRef.value) return
      
      try {
        await resetFormRef.value.validate()
        
        const response = await userService.resetPassword(
          resetForm.emailOrMobile,
          resetForm.code,
          resetForm.newPassword
        )
        
        if (response.code === 200) {
          ElMessage.success('密码重置成功')
          router.push('/auth/login')
        } else {
          ElMessage.error('密码重置失败')
        }
      } catch (error) {
        console.error('密码重置失败:', error)
        ElMessage.error('密码重置失败，请重试')
      }
    }
    
    const goToLogin = () => {
      router.push('/auth/login')
    }
    
    return {
      resetForm,
      resetRules,
      resetFormRef,
      countdown,
      sendResetCode,
      handleResetPassword,
      goToLogin
    }
  }
}
</script>

<style scoped>
.reset-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.reset-password-card {
  width: 450px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
}

.reset-password-header {
  text-align: center;
  margin-bottom: 20px;
}

.reset-password-header h2 {
  color: #303133;
  margin-bottom: 10px;
}

.reset-password-header p {
  color: #606266;
  font-size: 16px;
}

.reset-password-btn {
  width: 100%;
  margin-bottom: 10px;
}

.login-btn {
  width: 100%;
}
</style>