<template>
  <div class="event-detail-container">
    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="left-content">
        <!-- 节目基本信息 -->
        <div class="event-basic-info">
          <div class="event-poster">
            <img :src="eventDetail.image" alt="event poster" class="poster-img">
          </div>
          <div class="event-info">
            <div class="event-title">
              <span class="ticket-type">{{ eventDetail.ticketType }}</span>
              <h1>{{ eventDetail.title }}</h1>
            </div>
            <div class="event-meta">
              <p><strong>时间：</strong>{{ eventDetail.time }}</p>
              <p><strong>场馆：</strong>{{ eventDetail.venue }}</p>
            </div>
            
            <!-- 预售信息 -->
            <div class="presale-info">
              <div class="presale-tag">预售</div>
              <p>{{ eventDetail.presaleInfo }}</p>
            </div>
            
            <!-- 城市选择 -->
            <div class="city-selection">
              <span>城市：</span>
              <el-button type="primary" size="small">{{ eventDetail.city }}</el-button>
              <a href="#" class="view-more">查看更多</a>
            </div>
            
            <!-- 场次选择 -->
            <div class="session-selection">
              <span>场次：</span>
              <el-button type="primary" size="small">{{ eventDetail.session }}</el-button>
            </div>
            
            <!-- 票档选择 -->
            <div class="ticket-selection">
              <span>票档：</span>
              <div class="ticket-options">
                <el-button 
                  v-for="ticket in eventDetail.tickets" 
                  :key="ticket.id" 
                  :type="selectedTicketId === ticket.id ? 'primary' : 'default'" 
                  size="small"
                  @click="selectTicket(ticket)"
                >
                  {{ ticket.price }}({{ ticket.type }})
                </el-button>
              </div>
            </div>
            
            <!-- 数量选择 -->
            <div class="quantity-selection">
              <span>数量：</span>
              <el-input-number v-model="quantity" :min="1" :max="6" size="small"></el-input-number>
              <span class="quantity-tip">每笔订单限购6张</span>
            </div>
            
            <!-- 合计价格 -->
            <div class="total-price">
              <span>合计：</span>
              <span class="price">¥{{ totalPrice }}</span>
            </div>
            
            <!-- 立即购买按钮 -->
            <div class="buy-button">
              <el-button type="primary" size="large" @click="buyNow">立即购买</el-button>
            </div>
          </div>
        </div>
        
        <!-- 标签页 -->
        <div class="event-tabs">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="项目详情" name="detail">
              <div class="tab-content">
                <h3>活动介绍</h3>
                <p class="event-intro">{{ eventDetail.intro }}</p>
              </div>
            </el-tab-pane>
            <el-tab-pane label="购票须知" name="purchase">
              <div class="tab-content">
                <h3>购票须知</h3>
                <ul>
                  <li>购票时请确认演出时间、地点、票价</li>
                  <li>演出票一经售出，不予退换</li>
                  <li>请妥善保管好演出票，遗失不补</li>
                  <li>演出当天请提前30分钟入场</li>
                </ul>
              </div>
            </el-tab-pane>
            <el-tab-pane label="观演须知" name="attend">
              <div class="tab-content">
                <h3>观演须知</h3>
                <ul>
                  <li>演出期间请保持安静，不要随意走动</li>
                  <li>禁止携带食品、饮料入场</li>
                  <li>禁止使用闪光灯拍照</li>
                  <li>请将手机调至静音模式</li>
                </ul>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      
      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 购票提示 -->
        <div class="purchase-tips">
          <h4>购票提示</h4>
          <ul>
            <li>• 不支持退</li>
            <li>• 不支持选座</li>
            <li>• 电子票</li>
            <li>• 电子发票</li>
          </ul>
          <p class="invoice-info">发票开具：文化传播发展有限公司</p>
        </div>
        
        <!-- 为你推荐 -->
        <div class="recommendations">
          <h4>为你推荐</h4>
          <div class="recommendation-item" v-for="item in recommendations" :key="item.id">
            <img :src="item.image" alt="recommendation" class="recommendation-img">
            <div class="recommendation-info">
              <h5 class="recommendation-title">{{ item.title }}</h5>
              <p class="recommendation-venue">{{ item.venue }}</p>
              <p class="recommendation-price">¥{{ item.price }}起</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'EventDetail',
  setup() {
    const router = useRouter()
    const route = useRoute()
    
    const activeTab = ref('detail')
    const selectedTicketId = ref(1)
    const quantity = ref(1)
    
    const eventDetail = ref({
      id: 1,
      title: '周杰伦「嘉年华」世界巡回演唱会',
      ticketType: '电子票',
      time: '2025-08-20 周三 20:30',
      venue: '北京|工人体育馆',
      image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Jay%20Chou%20concert%20poster%20carnival%20colorful&image_size=portrait_4_3',
      presaleInfo: '本商品为预售商品，正式开票后将第一时间为您配票。一般于演出前1-2周开票，待预售期间，由主办方未正式开票，下单后无法立即为配票。一般于演出前1-2周开票，待正式开票后，请通过订单详情页或票夹详情，查看商品信息、取票方式等演出相关信息。',
      city: '北京',
      session: '2025-08-20 周三 20:30',
      tickets: [
        { id: 1, price: '580元', type: '看台' },
        { id: 2, price: '780元', type: '看台' },
        { id: 3, price: '980元', type: '看台' },
        { id: 4, price: '1280元', type: '看台' },
        { id: 5, price: '1680元', type: '内场' },
        { id: 6, price: '1880元', type: '内场' },
        { id: 7, price: '2000元', type: '内场' }
      ],
      intro: '华语流行乐坛一个难以逾越的丰碑。周杰伦，华语乐坛的天王级歌手，以其独特的音乐风格和创作才华，影响了整整一代人。他的歌曲融合了中国风、R&B、嘻哈等多种元素，创造了属于自己的音乐王国。2025年，周杰伦将带着他的「嘉年华」世界巡回演唱会回到北京，为粉丝们带来一场视听盛宴。'
    })
    
    const recommendations = ref([
      {
        id: 1,
        title: '拳力向未来',
        venue: '工人体育场',
        price: 580,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=boxing%20match%20poster&image_size=portrait_4_3'
      },
      {
        id: 2,
        title: '羽毛球大赛',
        venue: '北京体育馆',
        price: 700,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=badminton%20match%20poster&image_size=portrait_4_3'
      },
      {
        id: 3,
        title: '法语原版音乐剧《唐璜》',
        venue: '天桥艺术中心·大剧场',
        price: 225,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=musical%20don%20juan%20poster&image_size=portrait_4_3'
      }
    ])
    
    const selectedTicket = computed(() => {
      return eventDetail.value.tickets.find(ticket => ticket.id === selectedTicketId.value) || eventDetail.value.tickets[0]
    })
    
    const totalPrice = computed(() => {
      const price = parseInt(selectedTicket.value.price.replace('元', ''))
      return price * quantity.value
    })
    
    const selectTicket = (ticket) => {
      selectedTicketId.value = ticket.id
    }
    
    const buyNow = () => {
      // 跳转到订单创建页面
      router.push(`/order/create?eventId=${eventDetail.value.id}&ticketId=${selectedTicketId.value}&quantity=${quantity.value}`)
    }
    
    onMounted(() => {
      // 可以根据路由参数获取具体的节目信息
      if (route.params.id) {
        // 这里可以根据ID从API获取节目详情
        console.log('Event ID:', route.params.id)
      }
    })
    
    return {
      activeTab,
      eventDetail,
      selectedTicketId,
      quantity,
      totalPrice,
      recommendations,
      selectTicket,
      buyNow
    }
  }
}
</script>

<style scoped>
.event-detail-container {
  padding: 20px;
}

.main-content {
  display: flex;
  gap: 30px;
}

.left-content {
  flex: 1;
  min-width: 0;
}

.right-content {
  width: 300px;
  flex-shrink: 0;
}

/* 节目基本信息 */
.event-basic-info {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.event-poster {
  flex-shrink: 0;
  width: 300px;
  height: 400px;
  overflow: hidden;
  border-radius: 8px;
}

.poster-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.event-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.event-title {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.ticket-type {
  padding: 2px 8px;
  background-color: #e6f7ff;
  color: #1890ff;
  font-size: 12px;
  border-radius: 4px;
  flex-shrink: 0;
}

.event-title h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
  line-height: 1.3;
}

.event-meta p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

/* 预售信息 */
.presale-info {
  background-color: #fff7e6;
  border: 1px solid #ffd591;
  border-radius: 4px;
  padding: 10px;
  font-size: 14px;
  color: #d46b08;
}

.presale-tag {
  display: inline-block;
  padding: 2px 8px;
  background-color: #ff7a45;
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
  margin-bottom: 8px;
}

.presale-info p {
  margin: 0;
  line-height: 1.5;
}

/* 城市选择 */
.city-selection {
  display: flex;
  align-items: center;
  gap: 10px;
}

.city-selection span {
  font-size: 14px;
  color: #666;
}

.view-more {
  font-size: 14px;
  color: #409EFF;
  text-decoration: none;
}

.view-more:hover {
  text-decoration: underline;
}

/* 场次选择 */
.session-selection {
  display: flex;
  align-items: center;
  gap: 10px;
}

.session-selection span {
  font-size: 14px;
  color: #666;
}

/* 票档选择 */
.ticket-selection {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.ticket-selection span {
  font-size: 14px;
  color: #666;
}

.ticket-options {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 数量选择 */
.quantity-selection {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-selection span {
  font-size: 14px;
  color: #666;
}

.quantity-tip {
  font-size: 12px;
  color: #999;
}

/* 合计价格 */
.total-price {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.total-price span {
  font-size: 14px;
  color: #666;
}

.total-price .price {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}

/* 立即购买按钮 */
.buy-button {
  margin-top: 20px;
}

.buy-button .el-button {
  width: 200px;
  height: 48px;
  font-size: 16px;
}

/* 标签页 */
.event-tabs {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.tab-content {
  padding: 20px;
}

.tab-content h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #333;
}

.event-intro {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin: 0;
}

.tab-content ul {
  margin: 0;
  padding-left: 20px;
}

.tab-content li {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 8px;
}

/* 右侧内容 */
.purchase-tips {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.purchase-tips h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
}

.purchase-tips ul {
  margin: 0 0 15px 0;
  padding-left: 20px;
}

.purchase-tips li {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 8px;
}

.invoice-info {
  font-size: 12px;
  color: #999;
  margin: 0;
}

/* 为你推荐 */
.recommendations {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.recommendations h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
}

.recommendation-item {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.recommendation-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.recommendation-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.recommendation-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.recommendation-title {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #333;
  line-height: 1.3;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.recommendation-venue {
  margin: 0 0 5px 0;
  font-size: 12px;
  color: #666;
}

.recommendation-price {
  margin: 0;
  font-size: 14px;
  font-weight: bold;
  color: #f56c6c;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }
  
  .right-content {
    width: 100%;
  }
  
  .event-basic-info {
    flex-direction: column;
  }
  
  .event-poster {
    width: 100%;
    height: 300px;
  }
  
  .event-title h1 {
    font-size: 20px;
  }
  
  .ticket-options {
    justify-content: center;
  }
  
  .buy-button .el-button {
    width: 100%;
  }
}
</style>