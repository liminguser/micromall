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
      if (response.data.code === 200 && response.data.data) {
        const { token: newToken, ...userData } = response.data.data
        setToken(newToken)
        setUser(userData)
      }
      return response
    } catch (error) {
      logout()
      throw error
    }
  }

  // 注册
  const register = async (userData) => {
    try {
      const response = await axios.post('/api/auth/register', userData)
      return response
    } catch (error) {
      throw error
    }
  }

  // 设置用户信息
  const setUser = (userData) => {
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  // 设置token
  const setToken = (tokenValue) => {
    token.value = tokenValue
    localStorage.setItem('token', tokenValue)
    // 设置 axios 默认 headers
    if (tokenValue) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${tokenValue}`
    } else {
      delete axios.defaults.headers.common['Authorization']
    }
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