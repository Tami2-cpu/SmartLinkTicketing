<template>
  <div class="region-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>地区选择</h3>
        </div>
      </template>
      <el-form :model="regionForm" :rules="regionRules" ref="regionFormRef" label-width="80px">
        <el-form-item label="省份" prop="provinceId">
          <el-select v-model="regionForm.provinceId" placeholder="请选择省份" @change="handleProvinceChange">
            <el-option v-for="province in provinces" :key="province.id" :label="province.name" :value="province.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="城市" prop="cityId">
          <el-select v-model="regionForm.cityId" placeholder="请选择城市" @change="handleCityChange">
            <el-option v-for="city in cities" :key="city.id" :label="city.name" :value="city.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="区县" prop="districtId">
          <el-select v-model="regionForm.districtId" placeholder="请选择区县">
            <el-option v-for="district in districts" :key="district.id" :label="district.name" :value="district.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { baseService } from '../../services/base'

export default {
  name: 'Region',
  setup() {
    const regionFormRef = ref(null)
    const provinces = ref([])
    const cities = ref([])
    const districts = ref([])
    
    const regionForm = reactive({
      provinceId: '',
      cityId: '',
      districtId: ''
    })
    
    const regionRules = {
      provinceId: [
        { required: true, message: '请选择省份', trigger: 'blur' }
      ],
      cityId: [
        { required: true, message: '请选择城市', trigger: 'blur' }
      ],
      districtId: [
        { required: true, message: '请选择区县', trigger: 'blur' }
      ]
    }
    
    const fetchProvinces = async () => {
      try {
        const response = await baseService.getProvinces()
        if (response.code === 200) {
          provinces.value = response.data
        }
      } catch (error) {
        console.error('获取省份列表失败:', error)
        ElMessage.error('获取省份列表失败')
      }
    }
    
    const handleProvinceChange = async (provinceId) => {
      try {
        regionForm.cityId = ''
        regionForm.districtId = ''
        cities.value = []
        districts.value = []
        
        const response = await baseService.getCities(provinceId)
        if (response.code === 200) {
          cities.value = response.data
        }
      } catch (error) {
        console.error('获取城市列表失败:', error)
        ElMessage.error('获取城市列表失败')
      }
    }
    
    const handleCityChange = async (cityId) => {
      try {
        regionForm.districtId = ''
        districts.value = []
        
        const response = await baseService.getDistricts(cityId)
        if (response.code === 200) {
          districts.value = response.data
        }
      } catch (error) {
        console.error('获取区县列表失败:', error)
        ElMessage.error('获取区县列表失败')
      }
    }
    
    const submitForm = async () => {
      if (!regionFormRef.value) return
      
      try {
        await regionFormRef.value.validate()
        ElMessage.success('选择成功')
      } catch (error) {
        console.error('表单验证失败:', error)
      }
    }
    
    const resetForm = () => {
      if (regionFormRef.value) {
        regionFormRef.value.resetFields()
      }
      cities.value = []
      districts.value = []
    }
    
    onMounted(() => {
      fetchProvinces()
    })
    
    return {
      regionForm,
      regionRules,
      regionFormRef,
      provinces,
      cities,
      districts,
      handleProvinceChange,
      handleCityChange,
      submitForm,
      resetForm
    }
  }
}
</script>

<style scoped>
.region-container {
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
</style>