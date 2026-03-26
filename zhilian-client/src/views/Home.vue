<template>
  <div class="home-container">
    <!-- 精选活动展示 -->
    <div class="banner-section">
      <div class="featured-events">
        <h2 class="section-title">精选活动</h2>
        <div class="event-carousel">
          <div class="carousel-item" v-for="item in featuredEvents" :key="item.id">
            <img :src="item.image" alt="featured event" class="carousel-img">
            <div class="carousel-overlay">
              <h3 class="event-title">{{ item.title }}</h3>
              <p class="event-info">{{ item.time }} | {{ item.venue }}</p>
              <p class="event-price">¥{{ item.price }}起</p>
              <el-button type="primary" size="small">立即购票</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=concert%20icon%20simple%20flat&image_size=square" alt="演唱会" class="category-icon">
        <span>演唱会</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=theater%20icon%20simple%20flat&image_size=square" alt="话剧歌剧" class="category-icon">
        <span>话剧歌剧</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20icon%20simple%20flat&image_size=square" alt="体育" class="category-icon">
        <span>体育</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=children%20icon%20simple%20flat&image_size=square" alt="儿童亲子" class="category-icon">
        <span>儿童亲子</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=exhibition%20icon%20simple%20flat&image_size=square" alt="展览休闲" class="category-icon">
        <span>展览休闲</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=music%20concert%20icon%20simple%20flat&image_size=square" alt="音乐会" class="category-icon">
        <span>音乐会</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20performance%20icon%20simple%20flat&image_size=square" alt="曲苑杂坛" class="category-icon">
        <span>曲苑杂坛</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dance%20icon%20simple%20flat&image_size=square" alt="舞蹈芭蕾" class="category-icon">
        <span>舞蹈芭蕾</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=anime%20icon%20simple%20flat&image_size=square" alt="二次元" class="category-icon">
        <span>二次元</span>
      </div>
      <div class="category-item">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=travel%20icon%20simple%20flat&image_size=square" alt="旅游展览" class="category-icon">
        <span>旅游展览</span>
      </div>
    </div>

    <!-- 演唱会分类 -->
    <div class="event-category">
      <div class="category-header">
        <h3>演唱会</h3>
        <a href="#" class="view-all">查看全部</a>
      </div>
      <div class="event-grid">
        <div class="event-card" v-for="event in concertEvents" :key="event.id">
          <img :src="event.image" alt="event" class="event-img">
          <div class="event-info">
            <h4>{{ event.title }}</h4>
            <p class="event-time">{{ event.time }}</p>
            <p class="event-price">¥{{ event.price }}起</p>
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
  name: 'Home',
  setup() {
    const router = useRouter()
    
    const featuredEvents = ref([
      {
        id: 1,
        title: '【廊坊】魔术视界',
        time: '2025-05-26 18:30',
        venue: '廊坊大剧院',
        price: 150,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=magic%20show%20poster%20with%20female%20magician%20on%20stage&image_size=landscape_16_9'
      },
      {
        id: 2,
        title: '胡海泉「活在烟火里」',
        time: '2025-05-31 20:00',
        venue: '北京凯迪拉克中心',
        price: 280,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20stage%20performance&image_size=landscape_16_9'
      },
      {
        id: 3,
        title: '伍佰&China Blue',
        time: '2025-05-24 19:30',
        venue: '上海体育馆',
        price: 188,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=rock%20band%20concert%20poster%20live%20performance&image_size=landscape_16_9'
      }
    ])
    
    const concertEvents = ref([
      {
        id: 1,
        title: '【廊坊】魔术视界',
        time: '2025-05-26 18:30',
        price: 150,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=magic%20show%20poster%20with%20female%20magician&image_size=portrait_4_3'
      },
      {
        id: 2,
        title: '胡海泉「活在烟火里」',
        time: '2025-05-31 20:00',
        price: 280,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster&image_size=portrait_4_3'
      },
      {
        id: 3,
        title: '伍佰&China Blue',
        time: '2025-05-24 19:30',
        price: 188,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=rock%20band%20concert%20poster&image_size=portrait_4_3'
      },
      {
        id: 4,
        title: '林志炫《忘了我忘了我》',
        time: '2025-09-18 20:30',
        price: 288,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=male%20singer%20concert%20poster%20elegant&image_size=portrait_4_3'
      },
      {
        id: 5,
        title: '千禧年代的一十',
        time: '2025-04-30 19:30',
        price: 269,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=retro%20concert%20poster%202000s&image_size=portrait_4_3'
      },
      {
        id: 6,
        title: '《新桥恋人》',
        time: '2025-04-20 19:30',
        price: 180,
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=romantic%20concert%20poster&image_size=portrait_4_3'
      }
    ])

    return {
      featuredEvents,
      concertEvents
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 0;
}

/* 精选活动展示 */
.banner-section {
  padding: 40px 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #f0f0f0;
}

.featured-events {
  max-width: 1200px;
  margin: 0 auto;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 30px 0;
  text-align: center;
  font-weight: 600;
}

.event-carousel {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding-bottom: 20px;
  scrollbar-width: thin;
}

.event-carousel::-webkit-scrollbar {
  height: 6px;
}

.event-carousel::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.event-carousel::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.event-carousel::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.carousel-item {
  min-width: 350px;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
}

.carousel-item:hover {
  transform: translateY(-5px);
}

.carousel-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.carousel-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.8), rgba(0,0,0,0.3));
  color: white;
  padding: 20px;
}

.event-title {
  font-size: 18px;
  margin: 0 0 10px 0;
  font-weight: 600;
}

.event-info {
  font-size: 14px;
  margin: 0 0 10px 0;
  opacity: 0.9;
}

.event-price {
  font-size: 16px;
  margin: 0 0 15px 0;
  font-weight: bold;
  color: #ff6b6b;
}

.carousel-overlay .el-button {
  background-color: #409EFF;
  border: none;
}

.carousel-overlay .el-button:hover {
  background-color: #66b1ff;
}

/* 分类导航 */
.category-nav {
  display: flex;
  justify-content: center;
  padding: 30px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fff;
  flex-wrap: wrap;
  gap: 40px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s;
  width: 80px;
}

.category-item:hover {
  transform: translateY(-5px);
}

.category-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 10px;
}

.category-item span {
  font-size: 14px;
  color: #333;
  text-align: center;
}

/* 活动分类 */
.event-category {
  padding: 30px 20px;
  background-color: #f5f7fa;
  margin-bottom: 20px;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.category-header h3 {
  font-size: 20px;
  color: #333;
  margin: 0;
}

.view-all {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.view-all:hover {
  text-decoration: underline;
}

/* 活动网格 */
.event-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.event-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.event-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.event-info {
  padding: 15px;
}

.event-info h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.event-time {
  margin: 0 0 10px 0;
  font-size: 12px;
  color: #999;
  height: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.event-price {
  margin: 0;
  font-size: 14px;
  font-weight: bold;
  color: #f56c6c;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-section {
    padding: 30px 15px;
  }
  
  .section-title {
    font-size: 20px;
    margin-bottom: 20px;
  }
  
  .carousel-item {
    min-width: 280px;
  }
  
  .carousel-img {
    height: 160px;
  }
  
  .carousel-overlay {
    padding: 15px;
  }
  
  .event-title {
    font-size: 16px;
  }
  
  .event-info {
    font-size: 12px;
  }
  
  .event-price {
    font-size: 14px;
  }
  
  .category-nav {
    padding: 20px 15px;
    gap: 30px;
  }
  
  .category-item {
    width: 70px;
  }
  
  .category-icon {
    width: 40px;
    height: 40px;
  }
  
  .category-item span {
    font-size: 12px;
  }
  
  .event-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .event-info h4 {
    font-size: 12px;
  }
  
  .event-time {
    font-size: 10px;
  }
  
  .event-price {
    font-size: 12px;
  }
}
</style>