<template>
  <div class="product-list">
    <!-- 搜索和分类过滤区域 -->
    <div class="filter-section">
      <div class="filter-container">
        <el-row :gutter="20" class="filter-row">
          <el-col :xs="24" :sm="12" :md="8">
            <el-input
              v-model="searchQuery"
              placeholder="搜索你喜欢的商品"
              :prefix-icon="Search"
              class="search-input"
              @input="handleSearch"
            />
          </el-col>
          <el-col :xs="24" :sm="12" :md="8">
            <el-select
              v-model="selectedCategory"
              placeholder="选择分类"
              @change="handleCategoryChange"
              class="category-select"
            >
              <el-option
                v-for="category in categoryOptions"
                :key="category?.id"
                :label="category?.name"
                :value="category?.id"
              />
            </el-select>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <!-- 轮播图 -->
      <div class="carousel-container" v-else-if="validBanners.length > 0">
        <el-carousel 
          height="400px" 
          :interval="4000" 
          :arrow="'hover'" 
          indicator-position="outside"
          class="banner-carousel"
        >
          <el-carousel-item 
            v-for="banner in validBanners"
            :key="banner?.id"
          >
            <div class="banner-content" @click="banner?.id && navigateToDetail(banner.id)">
              <div class="banner-image">
                <el-image 
                  :src="banner?.image" 
                  class="carousel-image"
                  fit="contain"
                  lazy
                >
                  <template #error>
                    <div class="image-error">
                      <el-icon><PictureFilled /></el-icon>
                      <span>加载失败</span>
                    </div>
                  </template>
                  <template #placeholder>
                    <div class="image-placeholder">
                      <el-icon><Loading /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="carousel-content">
                <div class="content-wrapper">
                  <h3>{{ banner?.name }}</h3>
                  <p>{{ banner?.description }}</p>
                  <div class="price">¥{{ banner?.price }}</div>
                  <el-button type="primary" class="view-detail-btn" round>查看详情</el-button>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 商品列表 -->
      <div class="products-container">
        <h2 class="section-title" v-if="validProducts.length > 0">热门商品</h2>
        <el-empty v-if="validProducts.length === 0" description="暂无商品" />
        <el-row v-else :gutter="20">
          <el-col
            v-for="product in validProducts"
            :key="product?.id"
            :xs="12"
            :sm="8"
            :md="6"
            :lg="6"
            :xl="4"
            class="mb-4"
          >
            <el-card
              class="product-card"
              :body-style="{ padding: '0px' }"
              @click="product?.id && navigateToDetail(product.id)"
              shadow="hover"
            >
              <el-image 
                :src="product?.image" 
                class="product-image"
                fit="cover"
                lazy
              >
                <template #error>
                  <div class="image-error">
                    <el-icon><PictureFilled /></el-icon>
                    <span>加载失败</span>
                  </div>
                </template>
                <template #placeholder>
                  <div class="image-placeholder">
                    <el-icon><Loading /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="product-info">
                <h3 class="product-title">{{ product?.name }}</h3>
                <div class="product-price">¥{{ product?.price }}</div>
                <div class="product-desc">{{ product?.description }}</div>
                <el-button type="primary" class="buy-btn" text>立即购买</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Search, PictureFilled, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getProductList, getBannerProducts, getCategories } from '@/api/product'
import { debounce } from 'lodash-es'

const router = useRouter()
const searchQuery = ref('')
const selectedCategory = ref('')
const banners = ref([])
const categories = ref([])
const products = ref([])
const loading = ref(false)

// 计算属性：包含"全部"选项的分类列表
const categoryOptions = computed(() => {
  return [
    { id: '', name: '全部' },
    ...(categories.value || [])
  ]
})

// 处理图片URL
const getImageUrl = (url) => {
  if (!url || typeof url !== 'string') return ''
  // 如果已经是完整URL则直接返回
  if (url.startsWith('http')) return url
  // 如果是以/uploads开头，添加/api前缀
  if (url.startsWith('/uploads')) return `/api${url}`
  // 如果不是以/api开头，添加/api前缀
  return url.startsWith('/api') ? url : `/api${url}`
}

// 计算属性：过滤有效的banner并处理图片URL
const validBanners = computed(() => {
  const bannerList = Array.isArray(banners.value) ? banners.value.filter(banner => banner != null) : []
  return bannerList.map(banner => ({
    ...banner,
    image: getImageUrl(banner.image)
  }))
})

// 计算属性：过滤有效的products并处理图片URL
const validProducts = computed(() => {
  // 检查是否是分页数据
  let productList = []
  if (products.value && products.value.records) {
    productList = products.value.records.filter(product => product != null)
  } else {
    // 如果不是分页数据，检查是否是数组
    productList = Array.isArray(products.value) ? products.value.filter(product => product != null) : []
  }
  return productList.map(product => ({
    ...product,
    image: getImageUrl(product.image)
  }))
})

// 获取轮播图商品
const fetchBanners = async () => {
  try {
    const response = await getBannerProducts()
    console.log('Banner response:', response)
    banners.value = response?.data || []
  } catch (error) {
    console.error('获取轮播图失败:', error)
    ElMessage.error('获取轮播图失败')
    banners.value = []
  }
}

// 获取商品分类
const fetchCategories = async () => {
  try {
    const response = await getCategories()
    console.log('Categories response:', response)
    categories.value = response?.data || []
  } catch (error) {
    console.error('获取分类失败:', error)
    ElMessage.error('获取分类失败')
    categories.value = []
  }
}

// 获取商品列表
const fetchProducts = async () => {
  if (loading.value) return
  loading.value = true
  try {
    const params = {
      pageNum: 1,
      pageSize: 20,
      keyword: searchQuery.value,
      categoryId: selectedCategory.value,
    }
    const response = await getProductList(params)
    console.log('Products response:', response)
    products.value = response?.data || { records: [] }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
    products.value = { records: [] }
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = debounce(() => {
  fetchProducts()
}, 300)

// 处理分类变化
const handleCategoryChange = () => {
  fetchProducts()
}

// 跳转到商品详情
const navigateToDetail = (id) => {
  router.push(`/product/${id}`)
}

onMounted(() => {
  fetchBanners()
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.product-list {
  position: fixed;
  top: 60px;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: #f5f7fa;
}

.filter-section {
  position: relative;
  background-color: #fff;
  padding: 16px 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  z-index: 10;
  margin-bottom: 16px;
}

.filter-container {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #e4e7ed;
  border-radius: 8px;
  transition: all 0.3s;
}

.search-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--el-color-primary);
}

.category-select {
  width: 100%;
}

.category-select :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #e4e7ed;
  border-radius: 8px;
}

.filter-row {
  margin-bottom: 0;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  position: relative;
  padding: 0;
}

.main-content > * {
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  padding: 0 20px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 24px 0 16px;
  padding-left: 20px;
}

.carousel-container {
  margin: 0 auto 24px;
  max-width: 900px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.banner-carousel :deep(.el-carousel__indicators) {
  transform: translateY(-16px);
  z-index: 2;
}

.banner-carousel :deep(.el-carousel__indicator) {
  padding: 8px 4px;
}

.banner-carousel :deep(.el-carousel__button) {
  width: 30px;
  height: 3px;
  border-radius: 2px;
  background-color: rgba(255, 255, 255, 0.7);
}

.banner-carousel :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background-color: #fff;
}

.banner-carousel :deep(.el-carousel__arrow) {
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 50%;
  font-size: 20px;
}

.banner-carousel :deep(.el-carousel__container) {
  height: 360px !important;
}

.banner-image {
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.carousel-image {
  width: 100%;
  height: 100%;
  max-height: 360px;
}

.banner-content {
  position: relative;
  height: 100%;
  cursor: pointer;
  overflow: hidden;
}

.banner-content:hover .carousel-content {
  transform: translateY(0);
}

.carousel-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 24px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  transform: translateY(20px);
  transition: all 0.3s ease;
}

.content-wrapper {
  max-width: 500px;
}

.carousel-content h3 {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.carousel-content p {
  font-size: 16px;
  margin: 0 0 16px;
  opacity: 0.9;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.carousel-content .price {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.view-detail-btn {
  padding: 10px 20px;
  font-size: 14px;
}

.product-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  height: 100%;
  border: none;
  background-color: #fff;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: all 0.3s;
}

.product-info {
  padding: 12px;
}

.product-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #ff4d4f;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 6px;
}

.product-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 36px;
  line-height: 1.4;
}

.buy-btn {
  width: 100%;
  padding: 6px 0;
  font-size: 13px;
  border-radius: 4px;
}

.image-error,
.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
  color: #909399;
}

.loading-container {
  padding: 40px 20px;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 20px;
}

.el-empty {
  padding: 60px 0;
  background: #fff;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .product-list {
    top: 50px;
  }
  
  .filter-section {
    padding: 12px;
    margin-bottom: 12px;
  }
  
  .main-content {
    padding: 0;
  }
  
  .main-content > * {
    padding: 0 10px;
  }
  
  .section-title {
    font-size: 18px;
    margin: 16px 0 12px;
  }
  
  .carousel-container {
    margin: 0 auto 16px;
    border-radius: 6px;
  }

  .banner-carousel :deep(.el-carousel__container) {
    height: 200px !important;
  }
  
  .carousel-image {
    max-height: 200px;
  }

  .carousel-content {
    padding: 16px;
  }

  .carousel-content h3 {
    font-size: 18px;
    margin-bottom: 4px;
  }

  .section-title {
    font-size: 18px;
    margin: 16px 0 12px;
  }

  .product-image {
    height: 140px;
  }

  .product-info {
    padding: 10px;
  }

  .product-title {
    font-size: 14px;
    margin-bottom: 6px;
  }

  .product-price {
    font-size: 16px;
    margin-bottom: 4px;
  }

  .product-desc {
    font-size: 12px;
    margin-bottom: 8px;
    height: 32px;
  }

  .buy-btn {
    padding: 4px 0;
    font-size: 12px;
  }
}
</style> 