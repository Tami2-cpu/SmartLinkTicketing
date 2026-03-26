import api from './api'

export const orderService = {
  create(data) {
    return api.post('/order/create', data)
  },
  pay(data) {
    return api.post('/order/pay', data)
  },
  cancel(data) {
    return api.post('/order/cancel', data)
  },
  list() {
    return api.get('/order/list')
  },
  detail(id) {
    return api.get(`/order/detail?id=${id}`)
  },
  close(data) {
    return api.post('/order/close', data)
  }
}