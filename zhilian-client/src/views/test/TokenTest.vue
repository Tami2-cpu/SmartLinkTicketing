<template>
  <div class="token-test">
    <h2>Token 测试</h2>
    <div>
      <p>LocalStorage 中的 token: {{ token }}</p>
      <p>Token 长度: {{ tokenLength }}</p>
      <p>是否有 token: {{ hasToken }}</p>
      <el-button type="primary" @click="testGetInfo">测试获取用户信息</el-button>
      <el-button @click="clearToken">清除 token</el-button>
      <el-button @click="refreshToken">刷新 token</el-button>
    </div>
    <div v-if="testResult">
      <h3>测试结果:</h3>
      <pre>{{ testResult }}</pre>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { userService } from '../../services/user'
import { ElMessage } from 'element-plus'

export default {
  name: 'TokenTest',
  setup() {
    const token = ref('')
    const testResult = ref(null)
    
    const tokenLength = computed(() => token.value.length)
    const hasToken = computed(() => token.value && token.value.trim() !== '')
    
    const loadToken = () => {
      token.value = localStorage.getItem('token') || ''
    }
    
    const testGetInfo = async () => {
      try {
        testResult.value = '正在测试...'
        const response = await userService.getInfo()
        testResult.value = JSON.stringify(response, null, 2)
        ElMessage.success('测试成功')
      } catch (error) {
        testResult.value = JSON.stringify(error, null, 2)
        ElMessage.error('测试失败')
      }
    }
    
    const clearToken = () => {
      localStorage.removeItem('token')
      loadToken()
      ElMessage.success('Token 已清除')
    }
    
    const refreshToken = () => {
      loadToken()
      ElMessage.success('Token 已刷新')
    }
    
    onMounted(() => {
      loadToken()
    })
    
    return {
      token,
      tokenLength,
      hasToken,
      testResult,
      testGetInfo,
      clearToken,
      refreshToken
    }
  }
}
</script>

<style scoped>
.token-test {
  padding: 20px;
}

pre {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
  margin-top: 10px;
}
</style>