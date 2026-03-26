<template>
  <div class="payment-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>订单支付</h3>
        </div>
      </template>
      <div v-if="order" class="payment-info">
        <div class="order-summary">
          <h4>订单信息</h4>
          <el-descriptions :column="1">
            <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="总金额">¥{{ order.totalAmount }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ order.createTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="payment-method">
          <h4>选择支付方式</h4>
          <el-radio-group v-model="paymentForm.payType">
            <el-radio label="1">
              <div class="pay-option">
                <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=WeChat%20Pay%20logo&image_size=square" alt="微信支付" class="pay-logo">
                <span>微信支付</span>
              </div>
            </el-radio>
            <el-radio label="2">
              <div class="pay-option">
                <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Alipay%20logo&image_size=square" alt="支付宝" class="pay-logo">
                <span>支付宝</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
        <div class="payment-actions">
          <el-button type="primary" size="large" @click="handlePayment">立即支付</el-button>
          <el-button size="large" @click="goBack">取消</el-button>
        </div>
      </div>
      <div v-else class="loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { orderService } from '../../services/order'
import { paymentService } from '../../services/payment'
import { Loading } from '@element-plus/icons-vue'

export default {
  name: 'Pay',
  components: {
    Loading
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const order = ref(null)
    const loading = ref(true)
    
    const paymentForm = reactive({
      payType: '1' // 默认微信支付
    })
    
    const fetchOrderDetail = async () => {
      try {
        const id = route.params.id
        const response = await orderService.detail(id)
        if (response.code === 200) {
          order.value = response.data
        } else {
          ElMessage.error(response.message || '获取订单详情失败')
          router.push('/order/list')
        }
      } catch (error) {
        console.error('获取订单详情失败:', error)
        ElMessage.error('获取订单详情失败')
        router.push('/order/list')
      } finally {
        loading.value = false
      }
    }
    
    const handlePayment = async () => {
      try {
        let response
        if (paymentForm.payType === '1') {
          // 微信支付
          response = await paymentService.wxPay({
            orderId: order.value.id,
            amount: order.value.totalAmount
          })
        } else {
          // 支付宝
          response = await paymentService.aliPay({
            orderId: order.value.id,
            amount: order.value.totalAmount
          })
        }
        
        if (response.code === 200) {
          // 跳转到支付页面
          window.location.href = response.data.payUrl
        } else {
          ElMessage.error(response.message || '支付请求失败')
        }
      } catch (error) {
        console.error('支付请求失败:', error)
        ElMessage.error('支付请求失败，请重试')
      }
    }
    
    const goBack = () => {
      router.push(`/order/detail/${route.params.id}`)
    }
    
    onMounted(() => {
      fetchOrderDetail()
    })
    
    return {
      order,
      loading,
      paymentForm,
      handlePayment,
      goBack
    }
  }
}
</script>

<style scoped>
.payment-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.payment-info {
  margin-top: 20px;
}

.order-summary {
  margin-bottom: 30px;
}

.order-summary h4,
.payment-method h4 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.payment-method {
  margin-bottom: 30px;
}

.pay-option {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.pay-option:hover {
  border-color: #409EFF;
  background-color: #ecf5ff;
}

.pay-logo {
  width: 40px;
  height: 40px;
}

.payment-actions {
  margin-top: 30px;
  display: flex;
  gap: 10px;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  gap: 10px;
  font-size: 16px;
  color: #606266;
}

.loading .el-icon.is-loading {
  font-size: 24px;
  animation: loading 1s infinite linear;
}

@keyframes loading {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>