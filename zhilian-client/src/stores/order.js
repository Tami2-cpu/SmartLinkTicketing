import { defineStore } from 'pinia'
import { orderService } from '../services/order'

export const useOrderStore = defineStore('order', {
  state: () => ({
    orders: [],
    currentOrder: null,
    loading: false
  }),
  getters: {
    getOrders: (state) => state.orders,
    getCurrentOrder: (state) => state.currentOrder,
    getLoading: (state) => state.loading
  },
  actions: {
    async fetchOrders() {
      this.loading = true
      try {
        const response = await orderService.list()
        if (response.code === 200) {
          this.orders = response.data
        }
      } finally {
        this.loading = false
      }
    },
    async fetchOrderDetail(id) {
      this.loading = true
      try {
        const response = await orderService.detail(id)
        if (response.code === 200) {
          this.currentOrder = response.data
        }
      } finally {
        this.loading = false
      }
    },
    async createOrder(data) {
      this.loading = true
      try {
        const response = await orderService.create(data)
        if (response.code === 200) {
          return response.data
        }
        return null
      } finally {
        this.loading = false
      }
    }
  }
})