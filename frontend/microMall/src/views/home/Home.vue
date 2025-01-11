<template>
  <div class="home">
    <el-row :gutter="20">
      <!-- 左侧栏 -->
      <el-col :xs="24" :sm="12">
        <el-card class="left-card">
          <h2>欢迎来到 MicroMall</h2>
          <p>您已成功登录系统</p>
          <div class="stats">
            <el-statistic title="商品总数" :value="128" />
            <el-statistic title="用户数量" :value="256" />
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧栏 -->
      <el-col :xs="24" :sm="12">
        <el-card class="right-card">
          <template #header>
            <div class="card-header">
              <span>最新动态</span>
            </div>
          </template>
          <div class="activity-list">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :timestamp="activity.timestamp"
                :type="activity.type"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activities = ref([
  {
    content: '系统更新完成',
    timestamp: '2024-01-20',
    type: 'success'
  },
  {
    content: '新增商品分类功能',
    timestamp: '2024-01-19',
    type: 'info'
  },
  {
    content: '优化用户体验',
    timestamp: '2024-01-18',
    type: 'warning'
  }
])
</script>

<style scoped>
.home {
  padding: 20px;
}

.left-card, .right-card {
  height: 100%;
  margin-bottom: 20px;
}

.left-card {
  text-align: center;
  padding: 20px;
}

.left-card h2 {
  margin: 0 0 20px;
  color: var(--el-text-color-primary);
}

.left-card p {
  margin: 0 0 30px;
  color: var(--el-text-color-regular);
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-top: 30px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-list {
  padding: 10px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .left-card, .right-card {
    margin-bottom: 15px;
  }
  
  .stats {
    flex-direction: column;
    gap: 20px;
  }
}
</style> 