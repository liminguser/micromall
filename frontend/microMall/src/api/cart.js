import request from '@/utils/request'

// 添加商品到购物车
export function addToCart(productId, quantity = 1) {
  return request({
    url: '/cart/add',
    method: 'post',
    data: {
      productId,
      quantity
    }
  })
}

// 更新购物车商品数量
export function updateQuantity(productId, quantity) {
  return request({
    url: '/cart/update/quantity',
    method: 'put',
    data: {
      productId,
      quantity
    }
  })
}

// 删除购物车商品
export function removeFromCart(productId) {
  return request({
    url: '/cart/delete',
    method: 'delete',
    params: {
      productId
    }
  })
}

// 获取购物车列表
export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

// 更新商品选中状态
export function updateSelected(productId, selected) {
  return request({
    url: '/cart/update/selected',
    method: 'put',
    data: {
      productId,
      selected
    }
  })
}

// 更新全部商品选中状态
export function updateAllSelected(selected) {
  return request({
    url: '/cart/update/all-selected',
    method: 'put',
    data: {
      selected
    }
  })
}

// 清空购物车
export function clearCart() {
  return request({
    url: '/cart/clear',
    method: 'delete'
  })
} 