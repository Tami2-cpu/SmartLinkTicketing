import api from './api'

export const imageService = {
  upload(formData) {
    return api.post('/image/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  delete(data) {
    return api.delete('/image/delete', { data })
  },
  view(url) {
    return api.get('/image/view', { params: { url } })
  }
}