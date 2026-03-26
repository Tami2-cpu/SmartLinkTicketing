<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="register-header">
          <h2>智联票务系统</h2>
          <p>用户注册</p>
        </div>
      </template>
      <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="registerForm.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <el-row :gutter="10">
            <el-col :span="16">
              <el-input v-model="registerForm.captcha" placeholder="请输入验证码"></el-input>
            </el-col>
            <el-col :span="8">
              <img :src="captchaImage" alt="验证码" class="captcha-img" @click="refreshCaptcha">
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" class="register-btn">注册</el-button>
          <el-button @click="goToLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userService } from '../../services/user'
import { captchaService } from '../../services/captcha'
import { ElMessage } from 'element-plus'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const registerFormRef = ref(null)
    const captchaId = ref('')
    const captchaImage = ref('')
    
    const registerForm = reactive({
      username: '',
      password: '',
      confirmPassword: '',
      mobile: '',
      email: '',
      captcha: ''
    })
    
    const registerRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 4, max: 20, message: '用户名长度在 4 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== registerForm.password) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      mobile: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
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
    
    const handleRegister = async () => {
      if (!registerFormRef.value) return
      
      try {
        await registerFormRef.value.validate()
        
        // 验证验证码
        const captchaResponse = await captchaService.verify({
          captchaId: captchaId.value,
          captcha: registerForm.captcha
        })
        
        if (captchaResponse.code !== 200) {
          ElMessage.error('验证码错误')
          refreshCaptcha()
          return
        }
        
        // 注册
        const response = await userService.register({
          username: registerForm.username,
          password: registerForm.password,
          mobile: registerForm.mobile,
          email: registerForm.email
        })
        
        if (response.code === 200) {
          ElMessage.success('注册成功，请登录')
          router.push('/auth/login')
        } else {
          ElMessage.error(response.message || '注册失败，请重试')
          refreshCaptcha()
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请重试')
      }
    }
    
    const goToLogin = () => {
      router.push('/auth/login')
    }
    
    // 初始化验证码
    refreshCaptcha()
    
    return {
      registerForm,
      registerRules,
      registerFormRef,
      captchaImage,
      refreshCaptcha,
      handleRegister,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.register-card {
  width: 400px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
}

.register-header {
  text-align: center;
  margin-bottom: 20px;
}

.register-header h2 {
  color: #303133;
  margin-bottom: 10px;
}

.register-header p {
  color: #606266;
  font-size: 16px;
}

.captcha-img {
  width: 100%;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}

.register-btn {
  width: 100%;
  margin-bottom: 10px;
}

.login-btn {
  width: 100%;
}
</style>