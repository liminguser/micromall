import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCartList } from '@/api/cart'
import { useUserStore } from './user'

export const useCartStore = defineStore('cart', () => {
  const cartCount = ref(0)
  const userStore = useUserStore()

  // 获取购物车数量
  const fetchCartCount = async () => {
    if (!userStore.user?.id) {
      cartCount.value = 0
      return
    }
    
    try {
      const response = await getCartList(userStore.user.id)
      if (response.data.code === 200) {
        cartCount.value = response.data.data.length
      }
    } catch (error) {
      console.error('获取购物车数量失败:', error)
      cartCount.value = 0
    }
  }

  return {
    cartCount,
    fetchCartCount
  }
}) 