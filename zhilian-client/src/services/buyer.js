import api from './api'

export const buyerService = {
  add(data) {
    return api.post('/buyer/add', data)
  },
  list() {
    return api.get('/buyer/list')
  },
  detail(id) {
    return api.get(`/buyer/detail?id=${id}`)
  },
  update(data) {
    return api.put('/buyer/update', data)
  },
  delete(id) {
    return api.delete(`/buyer/delete?id=${id}`)
  }
}