<template>
  <div class="login-container">
    <el-card class="login-card" :body-style="{ padding: '30px 20px' }">
      <template #header>
        <h2 class="card-title">登录</h2>
      </template>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-position="top"
        @submit.prevent="handleSubmit"
        size="large"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <div class="form-actions">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        </div>

        <el-button
          type="primary"
          native-type="submit"
          :loading="loading"
          class="submit-btn"
          size="large"
        >
          登录
        </el-button>

        <div class="register-link">
          还没有账号？
          <el-link type="primary" @click="navigateTo('/auth/register')">
            立即注册
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const rememberMe = ref(false)

const formData = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    await userStore.login({
      ...formData,
      remember: rememberMe.value,
    })

    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.login-container {
  width: 100%;
}

.login-card {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.card-title {
  margin: 0;
  text-align: center;
  font-size: clamp(1.25rem, 3vw, 1.5rem);
  color: var(--el-text-color-primary);
  padding: 16px 0;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.submit-btn {
  width: 100%;
  margin-bottom: 20px;
  height: 44px;
  font-size: 16px;
}

.register-link {
  text-align: center;
  color: var(--el-text-color-regular);
  font-size: 14px;
}

:deep(.el-form-item__label) {
  padding-bottom: 8px;
  font-size: 15px;
}

:deep(.el-input__wrapper) {
  padding: 4px 11px;
}

:deep(.el-input__inner) {
  height: 36px;
}

@media screen and (max-width: 480px) {
  .login-card {
    border-radius: 0;
    box-shadow: none;
  }

  :deep(.el-card__header) {
    padding: 12px 20px;
  }

  .form-actions {
    margin-bottom: 20px;
  }

  .submit-btn {
    margin-bottom: 16px;
  }
}
</style> 