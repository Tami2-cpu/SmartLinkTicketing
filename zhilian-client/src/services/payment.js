import api from './api'

export const paymentService = {
  wxPay(data) {
    return api.post('/pay/wx', data)
  },
  aliPay(data) {
    return api.post('/pay/alipay', data)
  },
  getResult(orderId) {
    return api.get(`/pay/result?orderId=${orderId}`)
  }
}