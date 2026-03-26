<template>
  <div class="order-detail-container">
    <div class="order-header">
      <div class="order-no">订单号: {{ orderDetail.orderNo }}</div>
      <div class="order-status">
        <span class="status-text">{{ orderDetail.status }}</span>
        <span class="amount">实付款：¥{{ orderDetail.amount }}</span>
      </div>
    </div>

    <!-- 项目信息 -->
    <div class="section">
      <h3>项目信息</h3>
      <div class="project-table">
        <table>
          <thead>
            <tr>
              <th>项目信息</th>
              <th>座位信息</th>
              <th>单价</th>
              <th>数量</th>
              <th>优惠</th>
              <th>小计</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <div class="project-info">
                  <img :src="orderDetail.eventImage" alt="event" class="event-image">
                  <div class="event-details">
                    <div class="event-title">{{ orderDetail.eventTitle }}</div>
                    <div class="event-time">{{ orderDetail.eventTime }}</div>
                    <div class="event-venue">{{ orderDetail.eventVenue }}</div>
                  </div>
                </div>
              </td>
              <td>{{ orderDetail.seatInfo }}</td>
              <td>¥{{ orderDetail.unitPrice }}</td>
              <td>{{ orderDetail.quantity }}</td>
              <td>-</td>
              <td>{{ orderDetail.subtotal }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 信息表格 -->
    <div class="section">
      <div class="info-grid">
        <div class="info-column">
          <h4>配送信息</h4>
          <div class="info-item">
            <span class="info-label">配送方式：</span>
            <span class="info-value">{{ orderDetail.deliveryMethod }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">取票方式：</span>
            <span class="info-value">{{ orderDetail.pickupMethod }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">收货人：</span>
            <span class="info-value">{{ orderDetail.recipient }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">手机号：</span>
            <span class="info-value">{{ orderDetail.phone }}</span>
          </div>
        </div>

        <div class="info-column">
          <h4>订单信息</h4>
          <div class="info-item">
            <span class="info-label">订单编号：</span>
            <span class="info-value">{{ orderDetail.orderNo }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建时间：</span>
            <span class="info-value">{{ orderDetail.createTime }}</span>
          </div>
        </div>

        <div class="info-column">
          <h4>发票信息</h4>
          <div class="info-item">
            <span class="info-label">发票类型：</span>
            <span class="info-value">{{ orderDetail.invoiceType }}</span>
          </div>
        </div>

        <div class="info-column">
          <h4>金额明细</h4>
          <div class="info-item">
            <span class="info-label">商品总价：</span>
            <span class="info-value">¥{{ orderDetail.amount }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 购票人信息 -->
    <div class="section">
      <h3>购票人</h3>
      <div class="buyer-info">
        <div class="buyer-item">
          <div class="buyer-detail">
            <div class="buyer-name">购票人姓名: "{{ orderDetail.buyerName }}"</div>
            <div class="buyer-id">证件类型: {{ orderDetail.idType }}</div>
            <div class="buyer-id">证件号码: {{ orderDetail.idNumber }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="order-actions">
      <el-button type="primary" @click="goBack">返回列表</el-button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'OrderDetail',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const orderDetail = ref({
      orderNo: '1957014002638889870',
      status: '已支付',
      amount: 580,
      eventImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Jay%20Chou%20concert%20poster%20carnival&image_size=portrait_4_3',
      eventTitle: '周杰伦「嘉年华」世界巡回演唱会',
      eventTime: '2025-08-20 20:30:00',
      eventVenue: '工人体育馆',
      seatInfo: '暂无座位信息',
      unitPrice: 580,
      quantity: 1,
      subtotal: 580,
      deliveryMethod: '电子票',
      pickupMethod: '请使用购票人身份证直接入场',
      recipient: '11damaitest123',
      phone: '131****2525',
      createTime: '2025-08-17 17:37:52',
      invoiceType: '请在演出开始前，在程序上开具发票',
      buyerName: '华',
      idType: '身份证',
      idNumber: '2184********6730'
    })
    
    const goBack = () => {
      router.push('/order/list')
    }
    
    return {
      orderDetail,
      goBack
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  padding: 20px;
}

/* 订单头部 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e8e8e8;
}

.order-no {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.order-status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 10px;
}

.status-text {
  font-size: 16px;
  color: #52c41a;
  font-weight: 500;
}

.amount {
  font-size: 18px;
  color: #333;
  font-weight: bold;
}

/* 通用区块样式 */
.section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section h3 {
  margin: 0 0 20px 0;
  font-size: 16px;
  color: #333;
  font-weight: 500;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 10px;
}

/* 项目信息表格 */
.project-table {
  width: 100%;
  overflow-x: auto;
}

.project-table table {
  width: 100%;
  border-collapse: collapse;
}

.project-table th,
.project-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e8e8e8;
}

.project-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #333;
}

.project-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.event-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.event-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.event-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.event-time,
.event-venue {
  font-size: 12px;
  color: #666;
}

/* 信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
}

.info-column h4 {
  margin: 0 0 15px 0;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-label {
  font-size: 12px;
  color: #999;
}

.info-value {
  font-size: 14px;
  color: #333;
}

/* 购票人信息 */
.buyer-info {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 4px;
}

.buyer-item {
  display: flex;
  align-items: center;
}

.buyer-detail {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.buyer-name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.buyer-id {
  font-size: 14px;
  color: #666;
}

/* 操作按钮 */
.order-actions {
  margin-top: 30px;
  display: flex;
  justify-content: flex-start;
  gap: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .order-status {
    align-items: flex-start;
  }
  
  .info-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }
  
  .project-info {
    flex-direction: column;
    align-items: flex-start;
    text-align: left;
  }
  
  .event-image {
    width: 100px;
    height: 100px;
  }
}

@media (max-width: 480px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .project-table {
    font-size: 12px;
  }
  
  .project-table th,
  .project-table td {
    padding: 8px;
  }
}
</style>