<template>
  <div class="product-detail">
    <div class="detail-container" v-if="product">
      <el-row :gutter="40">
        <!-- 商品图片 -->
        <el-col :span="12">
          <div class="image-container">
            <img :src="product.image" :alt="product.name" class="product-image" />
          </div>
        </el-col>

        <!-- 商品信息 -->
        <el-col :span="12">
          <div class="info-container">
            <h1 class="product-name">{{ product.name }}</h1>
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-description">{{ product.description }}</div>
            
            <div class="product-stock">
              库存: <span :class="{ 'low-stock': product.stock < 10 }">{{ product.stock }}</span>
            </div>

            <div class="product-actions">
              <el-input-number
                v-model="quantity"
                :min="1"
                :max="product.stock"
                size="large"
                class="quantity-input"
              />
              <el-button
                type="primary"
                size="large"
                :disabled="product.stock <= 0"
                @click="handleAddToCart"
              >
                加入购物车
              </el-button>
              <el-button type="danger" size="large" :disabled="product.stock <= 0">
                立即购买
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 加载状态 -->
    <div v-else-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>

    <!-- 错误状态 -->
    <div v-else class="error-container">
      <el-empty description="商品不存在或已下架" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail } from '@/api/product'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { addToCart } from '@/api/cart'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const quantity = ref(1)
const loading = ref(true)
const userStore = useUserStore()
const cartStore = useCartStore()

// 获取商品详情
const fetchProductDetail = async () => {
  const productId = route.params.id
  if (!productId) {
    router.push('/products')
    return
  }

  try {
    const response = await getProductDetail(productId)
    if (response.data.code === 200) {
      product.value = response.data.data
      if (!product.value || product.value.status === 0) {
        ElMessage.error('商品不存在或已下架')
        product.value = null
      }
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    ElMessage.error('获取商品详情失败')
    product.value = null
  } finally {
    loading.value = false
  }
}

// 添加到购物车
const handleAddToCart = async () => {
  if (!userStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push('/auth/login')
    return
  }

  try {
    await addToCart(userStore.user.id, product.value.id, quantity.value)
    ElMessage.success('添加成功')
    // 更新购物车数量
    await cartStore.fetchCartCount()
  } catch (error) {
    console.error('添加到购物车失败:', error)
    ElMessage.error('添加到购物车失败')
  }
}

// 立即购买
const buyNow = () => {
  // TODO: 实现立即购买功能
  router.push('/order/confirm')
}

onMounted(() => {
  fetchProductDetail()
})
</script>

<style scoped>
.product-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.detail-container {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.image-container {
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 4px;
  overflow: hidden;
}

.product-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.info-container {
  padding: 20px 0;
}

.product-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.product-price {
  font-size: 28px;
  color: #f56c6c;
  margin-bottom: 20px;
}

.product-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 30px;
}

.product-stock {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.low-stock {
  color: #f56c6c;
}

.product-actions {
  display: flex;
  gap: 16px;
  margin-top: 24px;
}

.quantity-input {
  width: 120px;
}

.loading-container,
.error-container {
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
</style> 