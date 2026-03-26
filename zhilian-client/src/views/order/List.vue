<template>
  <div class="order-list-container">
    <div class="order-layout">
      <!-- 侧边栏 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h3>我的大麦</h3>
        </div>
        <div class="sidebar-menu">
          <div class="menu-item active">
            <h4>交易中心</h4>
            <div class="sub-menu">
              <div class="sub-menu-item active">订单管理</div>
            </div>
          </div>
          <div class="menu-item">
            <h4>账户中心</h4>
            <div class="sub-menu">
              <div class="sub-menu-item">账号设置</div>
              <div class="sub-menu-item">个人信息</div>
              <div class="sub-menu-item">常用购票人</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 主内容区 -->
      <div class="main-content">
        <div class="order-header">
          <h3>订单管理</h3>
        </div>
        
        <!-- 订单列表 -->
        <div class="order-list">
          <div class="order-item" v-for="order in orderList" :key="order.id">
            <div class="order-header-info">
              <span class="order-no">订单号: {{ order.orderNo }}</span>
            </div>
            <div class="order-content">
              <div class="order-event">
                <img :src="order.eventImage" alt="event" class="event-image">
                <div class="event-info">
                  <h4 class="event-title">{{ order.eventTitle }}</h4>
                  <p class="event-time">{{ order.eventTime }}</p>
                  <p class="event-venue">{{ order.eventVenue }}</p>
                </div>
              </div>
              <div class="order-stats">
                <div class="stat-item">
                  <span class="stat-label">票品张数</span>
                  <span class="stat-value">{{ order.ticketCount }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">订单金额</span>
                  <span class="stat-value">¥{{ order.totalAmount }}<br><span class="fee">（含运费 ¥0.00）</span></span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">交易状态</span>
                  <span class="stat-value status">{{ getStatusText(order.status) }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">交易操作</span>
                  <span class="stat-value">
                    <a href="#" @click.prevent="goToDetail(order.id)" class="order-detail-link">订单详情</a>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { orderService } from '../../services/order'

export default {
  name: 'OrderList',
  setup() {
    const router = useRouter()
    const orderList = ref([])
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    
    const fetchOrders = async () => {
      try {
        const response = await orderService.list()
        if (response.code === 200) {
          // 模拟订单数据，实际项目中应该从API获取
          orderList.value = [
            {
              id: 1,
              orderNo: '1957014002638889870',
              eventTitle: '周杰伦「嘉年华」世界巡回演唱会',
              eventTime: '2025-08-20 20:30',
              eventVenue: '工人体育馆',
              eventImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Jay%20Chou%20concert%20poster%20carnival&image_size=portrait_4_3',
              ticketCount: 1,
              totalAmount: 580,
              status: 1
            },
            {
              id: 2,
              orderNo: '195690244465026186',
              eventTitle: '韦礼安「一直都在」音乐会',
              eventTime: '2025-09-02 20:00',
              eventVenue: 'JDG英特尔电子竞技中心',
              eventImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20Beijing&image_size=portrait_4_3',
              ticketCount: 1,
              totalAmount: 288,
              status: 2
            },
            {
              id: 3,
              orderNo: '195688760376254802',
              eventTitle: '周杰伦「嘉年华」世界巡回演唱会',
              eventTime: '2025-08-20 20:30',
              eventVenue: '工人体育馆',
              eventImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Jay%20Chou%20concert%20poster%20carnival&image_size=portrait_4_3',
              ticketCount: 1,
              totalAmount: 2000,
              status: 2
            }
          ]
          total.value = orderList.value.length
        }
      } catch (error) {
        console.error('获取订单列表失败:', error)
        ElMessage.error('获取订单列表失败')
      }
    }
    
    const goToDetail = (id) => {
      router.push(`/order/detail/${id}`)
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '交易关闭',
        3: '已完成'
      }
      return statusMap[status] || '未知'
    }
    
    const handleSizeChange = (size) => {
      pageSize.value = size
      fetchOrders()
    }
    
    const handleCurrentChange = (current) => {
      currentPage.value = current
      fetchOrders()
    }
    
    onMounted(() => {
      fetchOrders()
    })
    
    return {
      orderList,
      total,
      currentPage,
      pageSize,
      fetchOrders,
      goToDetail,
      getStatusText,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.order-list-container {
  padding: 0;
}

.order-layout {
  display: flex;
  min-height: calc(100vh - 120px);
}

/* 侧边栏 */
.sidebar {
  width: 200px;
  background-color: #fff;
  border-right: 1px solid #e8e8e8;
  padding: 20px 0;
}

.sidebar-header {
  padding: 0 20px 20px;
  border-bottom: 1px solid #e8e8e8;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 18px;
  color: #ff4d4f;
  font-weight: bold;
}

.sidebar-menu {
  margin-top: 20px;
}

.menu-item {
  margin-bottom: 15px;
}

.menu-item h4 {
  padding: 0 20px;
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.sub-menu-item {
  padding: 8px 20px 8px 30px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.sub-menu-item:hover {
  color: #ff4d4f;
  background-color: rgba(255, 77, 79, 0.05);
}

.sub-menu-item.active {
  color: #ff4d4f;
  background-color: rgba(255, 77, 79, 0.1);
  font-weight: 500;
}

/* 主内容区 */
.main-content {
  flex: 1;
  padding: 30px;
  background-color: #f5f5f5;
}

.order-header {
  margin-bottom: 20px;
}

.order-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

/* 订单列表 */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.order-header-info {
  padding: 15px 20px;
  border-bottom: 1px solid #e8e8e8;
  background-color: #fafafa;
}

.order-no {
  font-size: 14px;
  color: #666;
}

.order-content {
  padding: 20px;
}

.order-event {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.event-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

.event-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.event-title {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.event-time,
.event-venue {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #666;
}

.order-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #e8e8e8;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 14px;
  color: #333;
}

.stat-value.status {
  color: #52c41a;
  font-weight: 500;
}

.fee {
  font-size: 12px;
  color: #999;
  font-weight: normal;
}

.order-detail-link {
  color: #ff4d4f;
  text-decoration: none;
  font-size: 14px;
}

.order-detail-link:hover {
  text-decoration: underline;
}

/* 分页 */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .order-layout {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #e8e8e8;
    padding: 15px 0;
  }
  
  .main-content {
    padding: 20px;
  }
  
  .order-stats {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .order-event {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .event-image {
    width: 150px;
    height: 150px;
  }
}
</style>