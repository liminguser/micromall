import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCartList } from '@/api/cart'
import { useUserStore } from './user'

export const useCartStore = defineStore('cart', () => {
  const cartCount = ref(0)
  const cartItems = ref([])
  const userStore = useUserStore()

  // 获取购物车数量
  const fetchCartCount = async () => {
    if (!userStore.isAuthenticated) {
      cartCount.value = 0
      return
    }
    
    try {
      const response = await getCartList()
      if (response?.data) {
        cartCount.value = response.data.length
      }
    } catch (error) {
      console.error('获取购物车数量失败:', error)
      cartCount.value = 0
    }
  }

  // 设置购物车商品
  const setCartItems = (items) => {
    cartItems.value = items
    cartCount.value = items.length
  }

  return {
    cartCount,
    cartItems,
    fetchCartCount,
    setCartItems
  }
}) 