<template>
  <div class="cart-page">
    <div class="cart-container">
      <!-- 购物车头部 -->
      <div class="cart-header">
        <h2>我的购物车</h2>

      </div>

      <!-- 购物车为空时显示 -->
      <el-empty v-if="cartItems.length === 0" description="购物车还是空的">
        <el-button type="primary" @click="$router.push('/products')">去购物</el-button>
      </el-empty>

      <!-- 购物车列表 -->
      <div v-else class="cart-content">
        <!-- 全选区域 -->
        <div class="select-all-bar">
          <el-checkbox
            v-model="allSelected"
            @change="handleSelectAll"
            :indeterminate="isIndeterminate"
          >
            全选
          </el-checkbox>
        </div>

        <!-- 商品列表 -->
        <el-card v-for="item in cartItems" :key="item.id" class="cart-item">
          <div class="item-content">
            <!-- 选择框 -->
            <el-checkbox
              v-model="item.selected"
              @change="(val) => handleSelectItem(item, val)"
            />

            <!-- 商品图片 -->
            <div class="item-image" @click="navigateToDetail(item.productId)">
              <el-image
                :src="item.product?.image"
                fit="cover"
                :preview-src-list="[item.product?.image]"
              >
                <template #error>
                  <div class="image-error">
                    <el-icon><PictureFilled /></el-icon>
                  </div>
                </template>
              </el-image>
            </div>

            <!-- 商品信息 -->
            <div class="item-info">
              <h3 class="item-name" @click="navigateToDetail(item.productId)">
                {{ item.product?.name }}
              </h3>
              <p class="item-desc">{{ item.product?.description }}</p>
              <div class="item-price">¥{{ item.product?.price }}</div>
            </div>

            <!-- 数量控制 -->
            <div class="item-quantity">
              <el-input-number
                v-model="item.quantity"
                :min="1"
                :max="item.product?.stock"
                @change="(val) => handleQuantityChange(item, val)"
              />
            </div>

            <!-- 小计 -->
            <div class="item-subtotal">
              <div class="subtotal-price">
                ¥{{ (item.quantity * item.product?.price).toFixed(2) }}
              </div>
              <el-button type="danger" text @click="handleRemoveItem(item)">
                删除
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- 底部结算栏 -->
        <div class="cart-footer">
          <div class="footer-left">
            <el-checkbox
              v-model="allSelected"
              @change="handleSelectAll"
              :indeterminate="isIndeterminate"
            >
              全选
            </el-checkbox>
            <el-button type="danger" text @click="handleClearCart">
              清空购物车
            </el-button>
          </div>
          <div class="footer-right">
            <div class="total-info">
              <span>已选择 {{ selectedCount }} 件商品</span>
              <span class="total-price">
                总计：¥{{ totalPrice.toFixed(2) }}
              </span>
            </div>
            <el-button type="primary" :disabled="selectedCount === 0">
              结算
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { ElMessage, ElMessageBox } from 'element-plus'
import { PictureFilled } from '@element-plus/icons-vue'
import {
  getCartList,
  updateQuantity,
  removeFromCart,
  updateSelected,
  updateAllSelected,
  clearCart
} from '@/api/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const cartItems = ref([])

// 获取购物车列表
const fetchCartList = async () => {
  try {
    const response = await getCartList(userStore.user.id)
    if (response.data.code === 200) {
      cartItems.value = response.data.data
    }
  } catch (error) {
    console.error('获取购物车失败:', error)
    ElMessage.error('获取购物车失败')
  }
}

// 计算全选状态
const allSelected = computed({
  get: () => {
    return cartItems.value.length > 0 && cartItems.value.every(item => item.selected)
  },
  set: (val) => {
    handleSelectAll(val)
  }
})

// 计算部分选中状态
const isIndeterminate = computed(() => {
  const selectedItems = cartItems.value.filter(item => item.selected)
  return selectedItems.length > 0 && selectedItems.length < cartItems.value.length
})

// 计算选中商品数量
const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).length
})

// 计算总价
const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((total, item) => total + item.quantity * item.product?.price, 0)
})

// 处理全选/取消全选
const handleSelectAll = async (val) => {
  try {
    await updateAllSelected(userStore.user.id, val)
    await fetchCartList()
  } catch (error) {
    console.error('更新选中状态失败:', error)
    ElMessage.error('更新选中状态失败')
  }
}

// 处理单个商品选中状态
const handleSelectItem = async (item, val) => {
  try {
    await updateSelected(userStore.user.id, item.productId, val)
    await fetchCartList()
  } catch (error) {
    console.error('更新选中状态失败:', error)
    ElMessage.error('更新选中状态失败')
  }
}

// 处理数量变化
const handleQuantityChange = async (item, val) => {
  try {
    await updateQuantity(userStore.user.id, item.productId, val)
    await fetchCartList()
    // 更新购物车数量
    await cartStore.fetchCartCount()
  } catch (error) {
    console.error('更新数量失败:', error)
    ElMessage.error('更新数量失败')
  }
}

// 处理删除商品
const handleRemoveItem = async (item) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await removeFromCart(userStore.user.id, item.productId)
    ElMessage.success('删除成功')
    // 更新购物车列表和数量
    await fetchCartList()
    await cartStore.fetchCartCount()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除商品失败:', error)
      ElMessage.error('删除商品失败')
    }
  }
}

// 处理清空购物车
const handleClearCart = async () => {
  try {
    await ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await clearCart(userStore.user.id)
    ElMessage.success('清空成功')
    // 更新购物车列表和数量
    await fetchCartList()
    await cartStore.fetchCartCount()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空购物车失败:', error)
      ElMessage.error('清空购物车失败')
    }
  }
}

// 跳转到商品详情
const navigateToDetail = (productId) => {
  router.push(`/product/${productId}`)
}

onMounted(() => {
  fetchCartList()
})
</script>

<style scoped>
.cart-page {
  padding: 20px;
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa;
}

.cart-container {
  max-width: 1200px;
  margin: 0 auto;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.cart-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.select-all-bar {
  background-color: #fff;
  padding: 12px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.cart-item {
  margin-bottom: 16px;
}

.cart-item :deep(.el-card__body) {
  padding: 20px;
}

.item-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.item-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}

.item-image .el-image {
  width: 100%;
  height: 100%;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-name {
  margin: 0 0 8px;
  font-size: 16px;
  color: #333;
  cursor: pointer;
}

.item-name:hover {
  color: var(--el-color-primary);
}

.item-desc {
  margin: 0 0 8px;
  font-size: 14px;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-price {
  font-size: 18px;
  color: #ff4d4f;
  font-weight: bold;
}

.item-quantity {
  width: 120px;
}

.item-subtotal {
  width: 120px;
  text-align: right;
}

.subtotal-price {
  font-size: 18px;
  color: #ff4d4f;
  font-weight: bold;
  margin-bottom: 8px;
}

.cart-footer {
  position: sticky;
  bottom: 0;
  background-color: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  margin-top: 20px;
  box-shadow: 0 -2px 12px 0 rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.total-info {
  text-align: right;
}

.total-info span {
  display: block;
  line-height: 1.5;
}

.total-price {
  font-size: 20px;
  color: #ff4d4f;
  font-weight: bold;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  color: #909399;
}

@media (max-width: 768px) {
  .cart-page {
    padding: 10px;
  }

  .cart-header h2 {
    font-size: 20px;
  }

  .item-content {
    flex-wrap: wrap;
    gap: 12px;
  }

  .item-image {
    width: 80px;
    height: 80px;
  }

  .item-info {
    width: calc(100% - 120px);
  }

  .item-quantity {
    width: 100%;
  }

  .item-subtotal {
    width: 100%;
    text-align: left;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .subtotal-price {
    margin-bottom: 0;
  }

  .cart-footer {
    flex-direction: column;
    gap: 16px;
  }

  .footer-left,
  .footer-right {
    width: 100%;
    justify-content: space-between;
  }
}
</style> 