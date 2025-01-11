import request from '@/utils/request'

// 更新用户信息
export const updateUserProfile = (userData) => {
  return request({
    url: '/users/info',
    method: 'put',
    data: userData
  })
}

// 上传头像
export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/upload/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
} 