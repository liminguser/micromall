<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <!-- Logo -->
        <div class="logo" @click="router.push('/products')">
          <h1>MicroMall</h1>
        </div>

        <!-- 导航菜单 -->
        <div class="nav-menu">
          <el-button
            text
            :class="{ active: route.meta.active === 'products' }"
            @click="router.push('/products')"
          >
            商品列表
          </el-button>
        </div>
      </div>

      <div class="nav-right">
        <!-- 购物车图标 -->
        <el-badge :value="cartStore.cartCount" :max="99" class="cart-badge" v-if="userStore.isAuthenticated">
          <el-button
            text
            class="cart-button"
            @click="router.push('/cart')"
            :class="{ active: route.path === '/cart' }"
          >
            <el-icon :size="24"><ShoppingCart /></el-icon>
          </el-button>
        </el-badge>

        <!-- 用户下拉菜单 -->
        <el-dropdown v-if="userStore.isAuthenticated" trigger="click">
          <el-button text class="user-button">
            <el-avatar :size="32" :src="userStore.user?.avatar">
              {{ userStore.user?.nickname?.charAt(0) || userStore.user?.username?.charAt(0) }}
            </el-avatar>
            <span class="username">{{ userStore.user?.nickname || userStore.user?.username }}</span>
            <el-icon><CaretBottom /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/profile')">
                <el-icon><User /></el-icon>个人中心
              </el-dropdown-item>
              <el-dropdown-item @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <!-- 登录按钮 -->
        <el-button v-else type="primary" @click="router.push('/auth/login')">
          登录
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { ElMessageBox } from 'element-plus'
import { 
  User, 
  SwitchButton, 
  CaretBottom,
  ShoppingCart
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

// 测试用户状态
console.log('User authenticated:', userStore.isAuthenticated)
console.log('User info:', userStore.user)

// 监听路由变化时更新购物车数量
watch(
  () => route.path,
  () => {
    if (userStore.isAuthenticated) {
      cartStore.fetchCartCount()
    }
  }
)

// 监听用户登录状态变化
watch(
  () => userStore.isAuthenticated,
  (newValue) => {
    if (newValue) {
      cartStore.fetchCartCount()
    } else {
      cartStore.cartCount = 0
    }
  }
)

// 处理退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.logout()
    router.push('/auth/login')
  } catch {
    // 用户取消退出登录
  }
}

onMounted(() => {
  if (userStore.isAuthenticated) {
    cartStore.fetchCartCount()
  }
})
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.nav-bar {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 40px;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: var(--el-color-primary);
}

.nav-menu {
  display: flex;
  gap: 20px;
}

.nav-menu .el-button {
  font-size: 16px;
}

.nav-menu .el-button.active {
  color: var(--el-color-primary);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cart-badge {
  margin-right: 8px;
}

.cart-button {
  padding: 8px;
  border-radius: 50%;
}

.cart-button:hover,
.cart-button.active {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.cart-button .el-icon {
  vertical-align: middle;
}

.user-button {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 40px;
  padding: 0 12px;
  border-radius: 20px;
}

.user-button:hover {
  background-color: var(--el-color-primary-light-9);
}

.username {
  margin: 0 4px;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.main-content {
  flex: 1;
  background-color: #f5f7fa;
}

@media (max-width: 768px) {
  .nav-bar {
    padding: 0 12px;
  }

  .nav-left {
    gap: 20px;
  }

  .logo h1 {
    font-size: 20px;
  }

  .nav-menu .el-button {
    font-size: 14px;
  }

  .username {
    display: none;
  }

  .user-button {
    padding: 0 8px;
  }
}
</style> 