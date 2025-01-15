import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  // 从 localStorage 恢复用户信息
  const initUser = () => {
    try {
      const userStr = localStorage.getItem('user')
      return userStr ? JSON.parse(userStr) : null
    } catch (error) {
      console.error('Error parsing user data from localStorage:', error)
      localStorage.removeItem('user')
      return null
    }
  }

  const user = ref(initUser())
  const token = ref(localStorage.getItem('token'))

  // 计算属性：判断用户是否已登录
  const isLoggedIn = computed(() => !!token.value && !!user.value)
  const isAuthenticated = computed(() => !!token.value && !!user.value)

  // 登录
  const login = async (credentials) => {
    try {
      const response = await axios.post('/api/auth/login', credentials)
      const { code, message, data } = response.data
      
      if (code !== 200 || !data) {
        throw new Error(message || '登录失败')
      }

      const { token: newToken, ...userData } = data
      if (!newToken || !userData) {
        throw new Error('登录响应数据无效')
      }

      setToken(newToken)
      setUser(userData)
      return response.data
    } catch (error) {
      logout()
      throw error.response?.data || error
    }
  }

  // 注册
  const register = async (userData) => {
    try {
      const response = await axios.post('/api/auth/register', userData)
      const { code, message } = response.data
      
      if (code !== 200) {
        throw new Error(message || '注册失败')
      }
      
      return response.data
    } catch (error) {
      throw error.response?.data || error
    }
  }

  // 设置用户信息
  const setUser = (userData) => {
    if (!userData || typeof userData !== 'object') {
      console.error('Invalid user data:', userData)
      return
    }
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  // 设置token
  const setToken = (tokenValue) => {
    if (!tokenValue || typeof tokenValue !== 'string') {
      console.error('Invalid token:', tokenValue)
      return
    }
    token.value = tokenValue
    localStorage.setItem('token', tokenValue)
    // 设置 axios 默认 headers
    axios.defaults.headers.common['Authorization'] = `Bearer ${tokenValue}`
  }

  // 登出
  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    delete axios.defaults.headers.common['Authorization']
  }

  // 初始化 axios token
  if (token.value) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
  }

  return {
    user,
    token,
    isLoggedIn,
    isAuthenticated,
    login,
    register,
    setUser,
    setToken,
    logout
  }
}) 