<template>
  <div class="image-upload-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>图片上传管理</h3>
        </div>
      </template>
      <el-upload
        class="image-uploader"
        action="/api/image/upload"
        list-type="picture-card"
        :on-success="handleSuccess"
        :on-error="handleError"
        :on-remove="handleRemove"
        multiple
        :limit="5"
        :on-exceed="handleExceed"
      >
        <el-icon class="upload-icon"><Plus /></el-icon>
        <div class="upload-text">点击上传</div>
      </el-upload>
      <el-dialog v-model="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="preview" />
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { imageService } from '../../services/image'
import { Plus } from '@element-plus/icons-vue'

export default {
  name: 'ImageUpload',
  components: {
    Plus
  },
  setup() {
    const dialogVisible = ref(false)
    const dialogImageUrl = ref('')
    
    const handleSuccess = (response) => {
      if (response.code === 200) {
        ElMessage.success('上传成功')
      } else {
        ElMessage.error('上传失败')
      }
    }
    
    const handleError = () => {
      ElMessage.error('上传失败')
    }
    
    const handleRemove = async (file, fileList) => {
      try {
        const response = await imageService.delete({ url: file.response.data.url })
        if (response.code === 200) {
          ElMessage.success('删除成功')
        } else {
          ElMessage.error('删除失败')
        }
      } catch (error) {
        console.error('删除图片失败:', error)
        ElMessage.error('删除失败，请重试')
      }
    }
    
    const handleExceed = () => {
      ElMessage.error('最多只能上传5张图片')
    }
    
    const handlePictureCardPreview = (file) => {
      dialogImageUrl.value = file.url
      dialogVisible.value = true
    }
    
    return {
      dialogVisible,
      dialogImageUrl,
      handleSuccess,
      handleError,
      handleRemove,
      handleExceed,
      handlePictureCardPreview
    }
  }
}
</script>

<style scoped>
.image-upload-container {
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

.image-uploader {
  margin-top: 20px;
}

.upload-icon {
  font-size: 28px;
  color: #909399;
}

.upload-text {
  margin-top: 8px;
  font-size: 14px;
  color: #606266;
}
</style>