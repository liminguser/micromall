<template>
  <div class="auth-layout">
    <div class="auth-container">
      <!-- Logo -->
      <div class="logo" @click="router.push('/products')">
        <h1>MicroMall</h1>
      </div>

      <!-- 主要内容区域 -->
      <router-view></router-view>

      <!-- 页脚 -->
      <div class="footer">
        <p>© 2024 MicroMall. All rights reserved.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.auth-layout {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 400px;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
  cursor: pointer;
}

.logo h1 {
  margin: 0;
  font-size: 28px;
  color: var(--el-color-primary);
}

.footer {
  margin-top: 40px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

@media screen and (max-width: 480px) {
  .auth-layout {
    padding: 0;
    background-color: #fff;
  }

  .logo {
    margin-bottom: 20px;
  }

  .logo h1 {
    font-size: 24px;
  }

  .footer {
    margin-top: 30px;
  }
}
</style> 