import request from '@/utils/request'

// 添加商品到购物车
export function addToCart(userId, productId, quantity = 1) {
  return request({
    url: '/cart/add',
    method: 'post',
    params: {
      userId,
      productId,
      quantity
    }
  })
}

// 更新购物车商品数量
export function updateQuantity(userId, productId, quantity) {
  return request({
    url: '/cart/update/quantity',
    method: 'put',
    params: {
      userId,
      productId,
      quantity
    }
  })
}

// 删除购物车商品
export function removeFromCart(userId, productId) {
  return request({
    url: '/cart/delete',
    method: 'delete',
    params: {
      userId,
      productId
    }
  })
}

// 获取购物车列表
export function getCartList(userId) {
  return request({
    url: '/cart/list',
    method: 'get',
    params: {
      userId
    }
  })
}

// 更新商品选中状态
export function updateSelected(userId, productId, selected) {
  return request({
    url: '/cart/update/selected',
    method: 'put',
    params: {
      userId,
      productId,
      selected
    }
  })
}

// 更新所有商品选中状态
export function updateAllSelected(userId, selected) {
  return request({
    url: '/cart/update/all-selected',
    method: 'put',
    params: {
      userId,
      selected
    }
  })
}

// 清空购物车
export function clearCart(userId) {
  return request({
    url: '/cart/clear',
    method: 'delete',
    params: {
      userId
    }
  })
} 