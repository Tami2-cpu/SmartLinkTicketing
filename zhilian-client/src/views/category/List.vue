<template>
  <div class="category-list-container">
    <!-- 筛选区域 -->
    <div class="filter-section">
      <!-- 城市选择 -->
      <div class="filter-row">
        <span class="filter-label">城市：</span>
        <div class="filter-options">
          <a href="#" class="filter-option active">全部</a>
          <a href="#" class="filter-option">北京</a>
          <a href="#" class="filter-option">合肥</a>
          <a href="#" class="filter-option">台湾</a>
          <a href="#" class="filter-option">澳门</a>
          <a href="#" class="filter-option">香港</a>
          <a href="#" class="filter-option">重庆</a>
          <a href="#" class="filter-option">衢州</a>
          <a href="#" class="filter-option">舟山</a>
          <a href="#" class="filter-option">温州</a>
          <a href="#" class="filter-option">台州</a>
          <a href="#" class="filter-option">绍兴</a>
          <a href="#" class="filter-option">宁波</a>
          <a href="#" class="filter-option">丽水</a>
          <a href="#" class="filter-option">金华</a>
          <a href="#" class="filter-option">嘉兴</a>
          <a href="#" class="filter-option">湖州</a>
          <a href="#" class="filter-option">杭州</a>
          <a href="#" class="filter-option">昭通</a>
          <a href="#" class="filter-option">玉溪</a>
          <a href="#" class="filter-option">西双版纳</a>
          <a href="#" class="filter-option">文山</a>
          <a href="#" class="filter-option">曲靖</a>
          <a href="#" class="filter-option more">更多</a>
        </div>
      </div>

      <!-- 分类选择 -->
      <div class="filter-row">
        <span class="filter-label">分类：</span>
        <div class="filter-options">
          <a href="#" class="filter-option">全部</a>
          <a href="#" class="filter-option active">演唱会</a>
          <a href="#" class="filter-option">话剧歌剧</a>
          <a href="#" class="filter-option">体育</a>
          <a href="#" class="filter-option">儿童亲子</a>
          <a href="#" class="filter-option">展览休闲</a>
          <a href="#" class="filter-option">音乐会</a>
          <a href="#" class="filter-option">曲苑杂坛</a>
          <a href="#" class="filter-option">舞蹈芭蕾</a>
          <a href="#" class="filter-option">二次元</a>
          <a href="#" class="filter-option">旅游展览</a>
        </div>
      </div>

      <!-- 时间选择 -->
      <div class="filter-row">
        <span class="filter-label">时间：</span>
        <div class="filter-options">
          <a href="#" class="filter-option active">全部</a>
          <a href="#" class="filter-option">今天</a>
          <a href="#" class="filter-option">明天</a>
          <a href="#" class="filter-option">本周末</a>
          <a href="#" class="filter-option">一个月内</a>
          <a href="#" class="filter-option">按日历</a>
        </div>
      </div>

      <!-- 排序选择 -->
      <div class="filter-row">
        <div class="filter-options sort-options">
          <a href="#" class="filter-option active">相关度排序</a>
          <a href="#" class="filter-option">推荐排序</a>
          <a href="#" class="filter-option">最近开场</a>
          <a href="#" class="filter-option">最新上架</a>
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 活动列表 -->
      <div class="event-list">
        <div class="event-item" v-for="event in events" :key="event.id" @click="goToEventDetail(event.id)">
          <div class="event-image">
            <img :src="event.image" alt="event" class="event-img">
          </div>
          <div class="event-info">
            <h4 class="event-title">{{ event.title }}</h4>
            <p class="event-artist">艺人：{{ event.artist }}</p>
            <p class="event-venue">{{ event.venue }}</p>
            <p class="event-time">{{ event.time }}</p>
            <div class="event-price">
              <span class="price-range">{{ event.priceRange }}</span>
              <span class="status">{{ event.status }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 推荐侧边栏 -->
      <div class="recommendation-sidebar">
        <h4>您可能还喜欢</h4>
        <div class="recommendation-item" v-for="item in recommendations" :key="item.id">
          <img :src="item.image" alt="recommendation" class="recommendation-img">
          <div class="recommendation-info">
            <h5 class="recommendation-title">{{ item.title }}</h5>
            <p class="recommendation-price">¥{{ item.price }}起</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'CategoryList',
  setup() {
    const router = useRouter()
    
    const events = ref([
      {
        id: 1,
        title: '[北京] 韦礼安「一直都在」音乐会',
        artist: '韦礼安',
        venue: '北京 | JDG英特尔电子竞技中心',
        time: '2025-09-02 周二 20:00',
        priceRange: '288-488元',
        status: '售票中',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20Beijing&image_size=portrait_4_3'
      },
      {
        id: 2,
        title: '[北京] 周华健「少年侠客」巡回演唱会',
        artist: '周华健',
        venue: '北京 | 疆进酒·OMNI SPACE',
        time: '2025-09-05 周五 20:30',
        priceRange: '156-299元',
        status: '售票中',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20traditional&image_size=portrait_4_3'
      },
      {
        id: 3,
        title: '[北京] 台偶OST专场【恶作剧之吻】— 台湾偶像剧主题曲演唱会 | 我想我会开始想念你',
        artist: '',
        venue: '北京 | IMAGINE 想象空间',
        time: '2025-08-21 周四 19:30',
        priceRange: '299-399元',
        status: '售票中',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=romantic%20concert%20poster%20group&image_size=portrait_4_3'
      }
    ])

    const recommendations = ref([
      {
        id: 1,
        title: '李荣浩「纵横四海」龙年深圳演唱会',
        price: 499,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20shenzhen&image_size=portrait_4_3'
      },
      {
        id: 2,
        title: '邓紫棋 I AM GLORIA世界巡回演唱会',
        price: 399,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=G.E.M.%20concert%20poster%20gloria&image_size=portrait_4_3'
      },
      {
        id: 3,
        title: '薛之谦「天外来物」巡回演唱会',
        price: 317,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20sci-fi&image_size=portrait_4_3'
      }
    ])

    const goToEventDetail = (eventId) => {
      router.push(`/event/detail/${eventId}`)
    }

    return {
      events,
      recommendations,
      goToEventDetail
    }
  }
}
</script>

<style scoped>
.category-list-container {
  padding: 20px;
  background-color: #fff;
  min-height: 100vh;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.filter-row {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.filter-label {
  font-size: 14px;
  color: #666;
  margin-right: 15px;
  white-space: nowrap;
}

.filter-options {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  flex: 1;
}

.filter-option {
  font-size: 14px;
  color: #666;
  text-decoration: none;
  padding: 2px 0;
  position: relative;
  white-space: nowrap;
}

.filter-option:hover {
  color: #409EFF;
}

.filter-option.active {
  color: #409EFF;
  font-weight: 500;
}

.filter-option.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #409EFF;
}

.filter-option.more {
  color: #999;
}

.sort-options {
  gap: 30px;
}

/* 主内容区 */
.main-content {
  display: flex;
  gap: 30px;
  margin-top: 20px;
}

/* 活动列表 */
.event-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.event-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  transition: all 0.3s;
  cursor: pointer;
}

.event-item:hover {
  border-color: #409EFF;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.event-image {
  flex-shrink: 0;
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 4px;
}

.event-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.event-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.event-title {
  margin: 0;
  font-size: 16px;
  color: #333;
  line-height: 1.4;
  font-weight: 500;
}

.event-artist {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.event-venue {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.event-time {
  margin: 0;
  font-size: 14px;
  color: #999;
}

.event-price {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: auto;
}

.price-range {
  font-size: 16px;
  font-weight: bold;
  color: #f56c6c;
}

.status {
  font-size: 14px;
  color: #67c23a;
}

/* 推荐侧边栏 */
.recommendation-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.recommendation-sidebar h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.recommendation-item {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  transition: all 0.3s;
}

.recommendation-item:hover {
  border-color: #409EFF;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
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
  margin: 0;
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
  
  .recommendation-sidebar {
    width: 100%;
  }
  
  .event-item {
    flex-direction: column;
  }
  
  .event-image {
    width: 100%;
    height: 200px;
  }
  
  .filter-options {
    gap: 15px;
  }
  
  .sort-options {
    gap: 20px;
  }
}
</style>