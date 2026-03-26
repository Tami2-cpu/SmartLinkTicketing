<template>
  <div class="ai-assistant-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>AI智能助手</h3>
        </div>
      </template>
      <div class="chat-container">
        <div class="chat-messages" ref="chatMessages">
          <div class="message system-message">
            <div class="message-content">
              <p>您好！我是智联票务的AI智能助手，有什么可以帮您的吗？</p>
            </div>
          </div>
          <div v-for="(message, index) in messages" :key="index" :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']">
            <div class="message-content">
              <p>{{ message.content }}</p>
            </div>
          </div>
          <div v-if="loading" class="message ai-message">
            <div class="message-content">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>正在思考...</span>
            </div>
          </div>
        </div>
        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            placeholder="请输入您的问题..."
            @keyup.enter="sendMessage"
            :disabled="loading"
          ></el-input>
          <el-button type="primary" @click="sendMessage" :disabled="loading">发送</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { Loading } from '@element-plus/icons-vue'

export default {
  name: 'Assistant',
  components: {
    Loading
  },
  setup() {
    const chatMessages = ref(null)
    const messages = ref([])
    const inputMessage = ref('')
    const loading = ref(false)
    
    const sendMessage = async () => {
      if (!inputMessage.value.trim() || loading.value) return
      
      const userMessage = inputMessage.value.trim()
      messages.value.push({ type: 'user', content: userMessage })
      inputMessage.value = ''
      
      // 滚动到底部
      await nextTick()
      scrollToBottom()
      
      loading.value = true
      
      // 模拟AI响应
      setTimeout(() => {
        const aiResponse = getAIResponse(userMessage)
        messages.value.push({ type: 'ai', content: aiResponse })
        loading.value = false
        
        // 滚动到底部
        nextTick(() => {
          scrollToBottom()
        })
      }, 1000)
    }
    
    const getAIResponse = (message) => {
      const lowerMessage = message.toLowerCase()
      
      if (lowerMessage.includes('你好') || lowerMessage.includes('您好')) {
        return '您好！我是智联票务的AI智能助手，很高兴为您服务。请问您需要了解什么信息？'
      } else if (lowerMessage.includes('演唱会') || lowerMessage.includes('音乐会')) {
        return '我们有周杰伦、林俊杰等知名歌手的演唱会门票，您可以在首页查看详情并购票。'
      } else if (lowerMessage.includes('体育') || lowerMessage.includes('比赛')) {
        return '我们提供NBA中国赛、中超联赛等体育赛事的门票，您可以在首页浏览相关信息。'
      } else if (lowerMessage.includes('展览') || lowerMessage.includes('博物馆')) {
        return '我们有敦煌莫高窟展览、故宫博物院特展等文化展览的门票，欢迎您前来参观。'
      } else if (lowerMessage.includes('购票') || lowerMessage.includes('买票')) {
        return '您可以在首页选择您感兴趣的活动，然后点击"立即购票"按钮进行购票。'
      } else if (lowerMessage.includes('退票') || lowerMessage.includes('退款')) {
        return '关于退票政策，您可以在订单详情页面查看具体的退票规则，或联系客服进行咨询。'
      } else if (lowerMessage.includes('客服') || lowerMessage.includes('联系')) {
        return '您可以通过客服电话400-123-4567联系我们，或在工作时间(9:00-18:00)在线咨询。'
      } else {
        return '感谢您的咨询。如果您有关于票务的问题，我很乐意为您解答。您可以询问演唱会、体育赛事、展览等相关信息，或者关于购票、退票的流程。'
      }
    }
    
    const scrollToBottom = () => {
      if (chatMessages.value) {
        chatMessages.value.scrollTop = chatMessages.value.scrollHeight
      }
    }
    
    onMounted(() => {
      scrollToBottom()
    })
    
    return {
      messages,
      inputMessage,
      loading,
      chatMessages,
      sendMessage
    }
  }
}
</script>

<style scoped>
.ai-assistant-container {
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

.chat-container {
  height: 600px;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.message {
  margin-bottom: 15px;
  display: flex;
}

.system-message {
  justify-content: center;
}

.system-message .message-content {
  background-color: #f5f7fa;
  border-radius: 10px;
  padding: 10px 15px;
  max-width: 80%;
  text-align: center;
}

.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  background-color: #409EFF;
  color: white;
  border-radius: 10px 10px 0 10px;
  padding: 10px 15px;
  max-width: 80%;
}

.ai-message {
  justify-content: flex-start;
}

.ai-message .message-content {
  background-color: #f5f7fa;
  border-radius: 0 10px 10px 10px;
  padding: 10px 15px;
  max-width: 80%;
  display: flex;
  align-items: center;
  gap: 10px;
}

.chat-input {
  padding: 20px;
  display: flex;
  gap: 10px;
}

.chat-input .el-input {
  flex: 1;
}

.ai-message .is-loading {
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