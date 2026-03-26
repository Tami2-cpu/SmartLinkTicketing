import { defineStore } from 'pinia'
import { userService } from '../services/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
    isLoggedIn: false
  }),
  getters: {
    getToken: (state) => state.token,
    getUserInfo: (state) => state.userInfo,
    getIsLoggedIn: (state) => state.isLoggedIn
  },
  actions: {
    async login(username, password) {
      const response = await userService.login({ username, password })
      if (response && response.code === 200 && response.data) {
        this.token = response.data.token
        this.userInfo = response.data.user
        this.isLoggedIn = true
        localStorage.setItem('token', response.data.token)
        return true
      }
      return false
    },
    async logout() {
      this.token = ''
      this.userInfo = null
      this.isLoggedIn = false
      localStorage.removeItem('token')
    },
    async getInfo() {
      try {
        console.log('userStore.getInfo() 被调用')
        const response = await userService.getInfo()
        console.log('userService.getInfo() 的响应:', response)
        if (response && response.code === 200 && response.data) {
          this.userInfo = response.data
          console.log('更新 userStore 中的 userInfo:', this.userInfo)
          return response.data
        }
        console.log('获取用户信息失败: 响应为空或格式错误')
        return null
      } catch (error) {
        console.error('获取用户信息失败:', error)
        return null
      }
    }
  }
})