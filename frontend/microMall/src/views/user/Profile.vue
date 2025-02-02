<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人中心</h2>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="profile-form"
      >
        <el-form-item label="头像" prop="avatar">
          <el-upload
            class="avatar-uploader"
            :action="null"
            :show-file-list="false"
            :http-request="customUpload"
            :before-upload="beforeAvatarUpload"
          >
            <el-avatar
              v-if="formData.avatar"
              :size="100"
              :src="getAvatarUrl(formData.avatar)"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="formData.username" disabled />
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="formData.nickname" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">保密</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="生日" prop="birthday">
          <el-date-picker
            v-model="formData.birthday"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { updateUserProfile, uploadAvatar } from '@/api/user'

const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  username: '',
  nickname: '',
  avatar: '',
  email: '',
  phone: '',
  gender: 0,
  birthday: '',
})

const rules = {
  nickname: [
    { max: 20, message: '昵称不能超过20个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 初始化表单数据
const initFormData = () => {
  const userData = userStore.user
  if (userData) {
    Object.keys(formData).forEach(key => {
      if (userData[key] !== undefined) {
        if (key === 'birthday' && userData[key]) {
          // 处理日期格式
          formData[key] = new Date(userData[key])
        } else {
          formData[key] = userData[key]
        }
      }
    })
  }
}

// 获取头像完整URL
const getAvatarUrl = (url) => {
  if (!url || typeof url !== 'string') return ''
  // 如果已经是完整URL则直接返回
  if (url.startsWith('http')) return url
  // 如果是以/uploads开头，添加/api前缀
  if (url.startsWith('/uploads')) return `/api${url}`
  // 如果不是以/api开头，添加/api前缀
  return url.startsWith('/api') ? url : `/api${url}`
}

// 处理头像上传成功
const handleAvatarSuccess = async (response) => {
  console.log('Avatar success response:', response) // 添加日志
  // 确保response是字符串类型的URL
  const avatarUrl = typeof response === 'string' ? response : response?.data
  if (!avatarUrl) {
    console.error('Invalid avatar URL:', response)
    ElMessage.error('获取头像URL失败')
    return
  }
  // 保存原始URL（不带/api前缀）
  formData.avatar = avatarUrl
  userStore.setUser({
    ...userStore.user,
    avatar: avatarUrl
  })
  ElMessage.success('头像上传成功')
}

// 头像上传前的验证
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

// 自定义上传方法
const customUpload = async (options) => {
  try {
    const response = await uploadAvatar(options.file)
    console.log('Upload response:', response) // 添加日志
    if (response?.code === 200) {
      handleAvatarSuccess(response.data)
      options.onSuccess(response.data)
    } else {
      throw new Error(response?.message || '上传失败')
    }
  } catch (error) {
    console.error('上传失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '上传失败'
    options.onError(new Error(errorMsg))
    ElMessage.error(errorMsg)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // 调用更新用户信息的API
    const response = await updateUserProfile(formData)
    
    // 更新本地存储的用户信息
    userStore.setUser({
      ...userStore.user,
      ...formData
    })
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error(error.response?.data?.message || '保存失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  initFormData()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.profile-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  color: var(--el-text-color-primary);
}

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar {
  display: block;
  cursor: pointer;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader-icon:hover {
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}

@media screen and (max-width: 480px) {
  .profile-container {
    padding: 10px;
  }

  .el-form-item {
    margin-bottom: 18px;
  }
}
</style> 