<template>
  <div class="order-create-container">
    <!-- 活动信息 -->
    <div class="event-info-section">
      <h1 class="event-title">{{ eventDetail.title }}</h1>
      <p class="event-venue">{{ eventDetail.venue }}</p>
      <p class="event-time">{{ eventDetail.time }}</p>
      <p class="event-ticket">{{ eventDetail.ticketType }}×{{ quantity }}张</p>
      <p class="event-seat">{{ eventDetail.seatInfo }}</p>
    </div>

    <!-- 服务说明 -->
    <div class="service-info">
      <span class="service-item">不支持退</span>
      <span class="service-item">不实名购票和入场</span>
      <span class="service-item">不支持选座</span>
      <span class="service-item">电子票</span>
      <span class="service-item">电子发票</span>
    </div>

    <!-- 实名观演人 -->
    <div class="buyer-section">
      <div class="section-header">
        <h3>实名观演人</h3>
        <el-button type="primary" size="small" @click="addBuyer">新增</el-button>
      </div>
      <p class="section-tip">仅需选择一位，入场时需携带对应证件</p>
      <div class="buyer-list">
        <div 
          class="buyer-item" 
          v-for="buyer in buyerList" 
          :key="buyer.id"
          @click="selectBuyer(buyer.id)"
          :class="{ active: selectedBuyerId === buyer.id }"
        >
          <div class="buyer-info">
            <p class="buyer-name">{{ buyer.name }}</p>
            <p class="buyer-idcard">身份证 {{ buyer.idCard }}</p>
          </div>
          <div class="buyer-checkbox" v-if="selectedBuyerId === buyer.id">
            <el-icon><Check /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 配送方式 -->
    <div class="delivery-section">
      <h3>配送方式</h3>
      <p class="delivery-tip">由于票品为价票务，非普通商品，其背后承载的文化服务具有时效性、稀缺性等特征，一旦订购成功，不支持退换。</p>
    </div>

    <!-- 价格信息 -->
    <div class="price-section">
      <div class="price-info">
        <span>¥{{ totalPrice }}</span>
        <a href="#" class="price-detail">明细</a>
      </div>
      <el-button type="primary" size="large" @click="createOrder">提交订单</el-button>
    </div>

    <!-- 新增购票人对话框 -->
    <el-dialog :title="'新增观演人'" v-model="dialogVisible" width="500px">
      <el-form :model="buyerForm" :rules="buyerRules" ref="buyerFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="buyerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="buyerForm.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="buyerForm.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBuyer">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { orderService } from '../../services/order'
import { buyerService } from '../../services/buyer'
import { Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'OrderCreate',
  components: {
    Check
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const orderFormRef = ref(null)
    const buyerFormRef = ref(null)
    const dialogVisible = ref(false)
    const selectedBuyerId = ref(null)
    const quantity = ref(1)
    const buyerList = ref([])
    
    const eventDetail = ref({
      title: '周杰伦「嘉年华」世界巡回演唱会',
      venue: '北京工人体育馆',
      time: '2025.08.20 周三 20:30',
      ticketType: '¥580票档',
      seatInfo: '按付款顺序配票，优先连座配票'
    })
    
    const buyerForm = reactive({
      name: '',
      idCard: '',
      mobile: ''
    })
    
    const buyerRules = {
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
      ],
      mobile: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ]
    }
    
    const totalPrice = computed(() => {
      return 580 * quantity.value
    })
    
    const fetchBuyers = async () => {
      try {
        const response = await buyerService.list()
        if (response.code === 200) {
          buyerList.value = response.data
          // 默认选择第一个购票人
          if (response.data.length > 0 && !selectedBuyerId.value) {
            selectedBuyerId.value = response.data[0].id
          }
        }
      } catch (error) {
        console.error('获取购票人列表失败:', error)
        ElMessage.error('获取购票人列表失败')
      }
    }
    
    const selectBuyer = (buyerId) => {
      selectedBuyerId.value = buyerId
    }
    
    const addBuyer = () => {
      // 重置表单
      Object.assign(buyerForm, {
        name: '',
        idCard: '',
        mobile: ''
      })
      dialogVisible.value = true
    }
    
    const saveBuyer = async () => {
      if (!buyerFormRef.value) return
      
      try {
        await buyerFormRef.value.validate()
        
        const response = await buyerService.add({
          name: buyerForm.name,
          idCard: buyerForm.idCard,
          mobile: buyerForm.mobile
        })
        
        if (response.code === 200) {
          ElMessage.success('添加成功')
          dialogVisible.value = false
          fetchBuyers()
        } else {
          ElMessage.error(response.message || '添加失败')
        }
      } catch (error) {
        console.error('添加购票人失败:', error)
        ElMessage.error('添加失败，请重试')
      }
    }
    
    const createOrder = async () => {
      if (!selectedBuyerId.value) {
        ElMessage.warning('请选择观演人')
        return
      }
      
      try {
        const response = await orderService.create({
          eventId: 1, // 这里应该根据实际情况获取
          items: [{
            buyerId: selectedBuyerId.value,
            ticketId: 1, // 这里应该根据实际情况获取
            seatInfo: eventDetail.value.seatInfo
          }]
        })
        
        if (response.code === 200) {
          ElMessage.success('订单创建成功')
          router.push(`/order/detail/${response.data.orderId}`)
        } else {
          ElMessage.error(response.message || '订单创建失败')
        }
      } catch (error) {
        console.error('创建订单失败:', error)
        ElMessage.error('创建订单失败，请重试')
      }
    }
    
    onMounted(() => {
      fetchBuyers()
      // 如果URL中有参数，自动填充
      if (route.query.quantity) {
        quantity.value = parseInt(route.query.quantity)
      }
    })
    
    return {
      eventDetail,
      buyerList,
      selectedBuyerId,
      quantity,
      totalPrice,
      dialogVisible,
      buyerForm,
      buyerRules,
      buyerFormRef,
      selectBuyer,
      addBuyer,
      saveBuyer,
      createOrder
    }
  }
}
</script>

<style scoped>
.order-create-container {
  padding: 0;
}

/* 活动信息 */
.event-info-section {
  background-color: #ff4d4f;
  color: #fff;
  padding: 30px;
}

.event-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 10px 0;
}

.event-venue {
  font-size: 16px;
  margin: 0 0 10px 0;
}

.event-time {
  font-size: 14px;
  margin: 0 0 10px 0;
}

.event-ticket {
  font-size: 14px;
  margin: 0 0 10px 0;
}

.event-seat {
  font-size: 14px;
  margin: 0;
}

/* 服务说明 */
.service-info {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 15px 30px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e8e8e8;
}

.service-item {
  font-size: 14px;
  color: #666;
}

/* 实名观演人 */
.buyer-section {
  padding: 30px;
  border-bottom: 1px solid #e8e8e8;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.section-tip {
  font-size: 14px;
  color: #666;
  margin: 0 0 20px 0;
}

.buyer-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.buyer-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.buyer-item:hover {
  border-color: #ff4d4f;
}

.buyer-item.active {
  border-color: #ff4d4f;
  background-color: rgba(255, 77, 79, 0.05);
}

.buyer-info {
  flex: 1;
}

.buyer-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0 0 5px 0;
}

.buyer-idcard {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.buyer-checkbox {
  color: #ff4d4f;
  font-size: 18px;
}

/* 配送方式 */
.delivery-section {
  padding: 30px;
  border-bottom: 1px solid #e8e8e8;
}

.delivery-section h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.delivery-tip {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.5;
}

/* 价格信息 */
.price-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
  background-color: #fafafa;
}

.price-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-info span {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
}

.price-detail {
  font-size: 14px;
  color: #666;
  text-decoration: none;
}

.price-detail:hover {
  color: #ff4d4f;
  text-decoration: underline;
}

.price-section .el-button {
  width: 200px;
  height: 48px;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .event-info-section {
    padding: 20px;
  }
  
  .event-title {
    font-size: 20px;
  }
  
  .service-info {
    padding: 15px 20px;
  }
  
  .buyer-section,
  .delivery-section,
  .price-section {
    padding: 20px;
  }
  
  .price-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .price-section .el-button {
    width: 100%;
  }
}
</style>