import request from '@/utils/request'

export function getProductList(params) {
  return request({
    url: '/products',
    method: 'get',
    params
  })
}

export function getProductDetail(id) {
  return request({
    url: `/products/${id}`,
    method: 'get'
  })
}

export function getBannerProducts() {
  return request({
    url: '/products/banners',
    method: 'get'
  })
}

export function getCategories() {
  return request({
    url: '/products/categories',
    method: 'get'
  })
} 