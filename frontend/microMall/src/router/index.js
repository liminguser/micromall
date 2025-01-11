import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/products'
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('@/views/user/Profile.vue'),
      },
      {
        path: 'products',
        name: 'products',
        component: () => import('@/views/product/ProductList.vue'),
        meta: { active: 'products' }
      },
      {
        path: 'product/:id',
        name: 'product-detail',
        component: () => import('@/views/product/ProductDetail.vue'),
        meta: { active: 'products' }
      },
      {
        path: 'cart',
        name: 'cart',
        component: () => import('@/views/cart/Cart.vue'),
        meta: { 
          title: '购物车',
          requiresAuth: true
        }
      }
    ],
  },
  {
    path: '/auth',
    component: () => import('@/layout/AuthLayout.vue'),
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/user/Login.vue'),
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('@/views/user/Register.vue'),
      },
    ],
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const publicPages = ['/auth/login', '/auth/register']
  const authRequired = !publicPages.includes(to.path)
  const userStore = useUserStore()

  if (authRequired && !userStore.isAuthenticated) {
    return next('/auth/login')
  }

  if (!authRequired && userStore.isAuthenticated) {
    return next('/')
  }

  next()
})

export default router 