<template>
  <div class="register-container">
    <el-card class="register-card" :body-style="{ padding: '30px 20px' }">
      <template #header>
        <h2 class="card-title">注册</h2>
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

        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="formData.phone"
            placeholder="请输入手机号"
            :prefix-icon="Phone"
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

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item class="agreement-item">
          <el-checkbox v-model="formData.agreement" class="agreement">
            我已阅读并同意
            <el-link type="primary" @click="showAgreement">用户协议</el-link>
            和
            <el-link type="primary" @click="showPrivacy">隐私政策</el-link>
          </el-checkbox>
        </el-form-item>

        <el-button
          type="primary"
          native-type="submit"
          :loading="loading"
          class="submit-btn"
          size="large"
        >
          注册
        </el-button>

        <div class="login-link">
          已有账号？
          <el-link type="primary" @click="navigateTo('/auth/login')">
            立即登录
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
import { User, Lock, Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  agreement: false,
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (formData.confirmPassword !== '') {
      formRef.value?.validateField('confirmPassword')
    }
    callback()
  }
}

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== formData.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{4,16}$/, message: '用户名必须是4-16位字母或数字', trigger: 'blur' },
  ],
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{6,16}$/, message: '密码必须是6-16位字母或数字', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
  agreement: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请阅读并同意用户协议和隐私政策'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const response = await userStore.register(formData)

    if (response.data.code === 200) {
      ElMessage.success('注册成功')
      router.push('/auth/login')
    } else {
      ElMessage.error(response.data.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error(error.response?.data?.message || '注册失败')
  } finally {
    loading.value = false
  }
}

const navigateTo = (path) => {
  router.push(path)
}

const showAgreement = () => {
  // TODO: 显示用户协议
}

const showPrivacy = () => {
  // TODO: 显示隐私政策
}
</script>

<style scoped>
.register-container {
  width: 100%;
}

.register-card {
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

.agreement-item {
  margin-bottom: 24px;
}

.agreement {
  font-size: 14px;
  line-height: 1.5;
}

.submit-btn {
  width: 100%;
  margin-bottom: 20px;
  height: 44px;
  font-size: 16px;
}

.login-link {
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
  .register-card {
    border-radius: 0;
    box-shadow: none;
  }

  :deep(.el-card__header) {
    padding: 12px 20px;
  }

  .agreement-item {
    margin-bottom: 20px;
  }

  .submit-btn {
    margin-bottom: 16px;
  }
}
</style> 