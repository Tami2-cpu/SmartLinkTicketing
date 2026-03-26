import api from './api'

export const userService = {
  register(data) {
    return api.post('/user/register', data)
  },
  login(data) {
    return api.post('/user/login', data)
  },

  getInfo() {
    return api.get('/user/info')
  },
  update(data) {
    return api.put('/user/update', data)
  },
  sendResetCode(emailOrMobile) {
    return api.post('/user/send-reset-code', null, {
      params: { emailOrMobile }
    })
  },
  resetPassword(emailOrMobile, code, newPassword) {
    return api.post('/user/reset-password', null, {
      params: { emailOrMobile, code, newPassword }
    })
  },
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/user/upload-avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}