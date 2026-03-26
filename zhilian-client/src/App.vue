<template>
  <div class="app-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="logo">
            <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=JavaUp%20logo%20with%20koala%20icon%20simple%20modern%20blue%20color&image_size=square" alt="JavaUp" class="logo-img">
          </div>
          <div class="city-selector">
            <el-dropdown @command="handleCitySelect">
              <span class="city-dropdown">
                {{ currentCity }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="北京">北京</el-dropdown-item>
                  <el-dropdown-item command="上海">上海</el-dropdown-item>
                  <el-dropdown-item command="广州">广州</el-dropdown-item>
                  <el-dropdown-item command="深圳">深圳</el-dropdown-item>
                  <el-dropdown-item command="杭州">杭州</el-dropdown-item>
                  <el-dropdown-item command="成都">成都</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="nav-section">
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
              <el-menu-item index="/">首页</el-menu-item>
              <el-menu-item index="/category/list">分类</el-menu-item>
            </el-menu>
          </div>
          <div class="search-section">
            <el-input v-model="searchQuery" placeholder="搜索明星、演出、体育赛事" class="search-input">
              <template #append>
                <el-button type="primary" class="search-button">搜索</el-button>
              </template>
            </el-input>
          </div>
          <div class="user-section">
            <div class="login-btn" v-if="!isLoggedIn">
              <el-button type="text" @click="goToLogin">登录</el-button>
            </div>
            <div class="user-info" v-else>
              <el-dropdown>
                <span class="user-dropdown">
                  {{ userInfo?.nickname || userInfo?.username }}
                  <el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
                    <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div class="doc-btn">
              <el-button type="text">文档</el-button>
            </div>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer>
        <div class="footer-content">
          <p>© 2026 智联票务系统 版权所有</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from './stores/user'
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    ArrowDown
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    
    const searchQuery = ref('')
    const currentCity = ref('北京')
    const activeIndex = computed(() => {
      return route.path || '/'
    })
    
    const isLoggedIn = computed(() => {
      return userStore.isLoggedIn
    })
    
    const userInfo = computed(() => {
      return userStore.userInfo
    })
    
    const handleSelect = (key) => {
      router.push(key)
    }
    
    const goToLogin = () => {
      router.push('/auth/login')
    }
    
    const goToRegister = () => {
      router.push('/auth/register')
    }
    
    const goToProfile = () => {
      router.push('/user/profile')
    }
    
    const handleLogout = async () => {
      await userStore.logout()
      router.push('/')
    }
    
    const handleCitySelect = (city) => {
      currentCity.value = city
    }
    
    onMounted(() => {
      // 检查登录状态
      if (userStore.token) {
        userStore.getInfo()
      }
    })
    
    return {
      activeIndex,
      isLoggedIn,
      userInfo,
      searchQuery,
      currentCity,
      handleSelect,
      goToLogin,
      goToRegister,
      goToProfile,
      handleLogout,
      handleCitySelect
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  min-height: 100vh;
}

.el-header {
  background-color: white;
  color: #333;
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.header-content {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  border-bottom: 1px solid #f0f0f0;
  gap: 20px;
}

.logo {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 100px;
  height: 40px;
  object-fit: contain;
}

.city-selector {
  display: flex;
  align-items: center;
}

.city-dropdown {
  cursor: pointer;
  color: #333;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.nav-section {
  display: flex;
  align-items: center;
}

.el-menu-demo {
  background-color: white;
  border-bottom: none;
  height: 40px;
  line-height: 40px;
}

.el-menu-demo .el-menu-item {
  color: #333;
  height: 40px;
  line-height: 40px;
  margin-right: 20px;
  font-size: 14px;
}

.el-menu-demo .el-menu-item.is-active {
  color: #ff4d4f;
  background-color: transparent;
  border-bottom: none;
  font-weight: bold;
}

.search-section {
  flex: 1;
  display: flex;
  align-items: center;
  max-width: 500px;
  margin: 0 20px;
}

.search-input {
  flex: 1;
  height: 36px;
  border-radius: 0;
  border: 1px solid #d9d9d9;
  border-right: none;
}

.search-button {
  height: 36px;
  border-radius: 0;
  background-color: #ff4d4f;
  border-color: #ff4d4f;
  width: 80px;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.login-btn {
  display: flex;
  align-items: center;
}

.doc-btn {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-dropdown {
  color: #333;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.el-main {
  padding: 0;
  background-color: #f5f7fa;
  min-height: calc(100vh - 160px);
}

.el-footer {
  background-color: #f5f7fa;
  color: #606266;
  text-align: center;
  padding: 20px;
  height: 60px;
}

.footer-content {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.footer-content p {
  margin: 0;
}
</style>