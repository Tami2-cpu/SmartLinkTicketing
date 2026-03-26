import api from './api'

export const baseService = {
  getProvinces() {
    return api.get('/base/region/province')
  },
  getCities(parentId) {
    return api.get(`/base/region/city?parentId=${parentId}`)
  },
  getDistricts(parentId) {
    return api.get(`/base/region/district?parentId=${parentId}`)
  },
  getChannels() {
    return api.get('/base/channel/list')
  },
  addChannel(data) {
    return api.post('/base/channel/add', data)
  }
}