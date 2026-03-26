<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>智联票务系统</h2>
          <p>用户登录</p>
        </div>
      </template>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input v-model="loginForm.captcha" placeholder="请输入验证码"></el-input>
            </el-col>
            <el-col :span="8">
              <img :src="captchaImage" alt="验证码" class="captcha-img" @click="refreshCaptcha">
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
          <el-button @click="goToRegister" class="register-btn">注册</el-button>
        </el-form-item>
        <el-form-item class="forget-password">
          <a href="#" @click.prevent="forgetPassword">忘记密码？</a>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { captchaService } from '../../services/captcha'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const loginFormRef = ref(null)
    const captchaId = ref('')
    const captchaImage = ref('')
    
    const loginForm = reactive({
      username: '',
      password: '',
      captcha: ''
    })
    
    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
      captcha: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
      ]
    }
    
    const refreshCaptcha = async () => {
      try {
        const response = await captchaService.generate()
        if (response.code === 200) {
          captchaId.value = response.data.captchaId
          captchaImage.value = response.data.imageBase64
        }
      } catch (error) {
        console.error('获取验证码失败:', error)
      }
    }
    
    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      try {
        await loginFormRef.value.validate()
        
        // 验证验证码
        const captchaResponse = await captchaService.verify({
          captchaId: captchaId.value,
          captcha: loginForm.captcha
        })
        
        if (captchaResponse.code !== 200) {
          ElMessage.error('验证码错误')
          refreshCaptcha()
          return
        }
        
        // 登录
        const success = await userStore.login(loginForm.username, loginForm.password)
        if (success) {
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error('登录失败，请检查用户名和密码')
          refreshCaptcha()
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请重试')
      }
    }
    
    const goToRegister = () => {
      router.push('/auth/register')
    }
    
    const forgetPassword = () => {
      router.push('/auth/reset-password')
    }
    
    // 初始化验证码
    refreshCaptcha()
    
    return {
      loginForm,
      loginRules,
      loginFormRef,
      captchaImage,
      refreshCaptcha,
      handleLogin,
      goToRegister,
      forgetPassword
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-header h2 {
  color: #303133;
  margin-bottom: 10px;
}

.login-header p {
  color: #606266;
  font-size: 16px;
}

.captcha-img {
  width: 100%;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  margin-bottom: 10px;
}

.register-btn {
  width: 100%;
}

.forget-password {
  text-align: right;
  margin-top: 10px;
}

.forget-password a {
  color: #409EFF;
  text-decoration: none;
}

.forget-password a:hover {
  text-decoration: underline;
}
</style>