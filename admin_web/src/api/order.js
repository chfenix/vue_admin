import request from '@/utils/request'

export function createOrder(data) {
  return request({
    url: '/api/order/create',
    method: 'post',
    data
  })
}

export function getList(params) {
  return request({
    url: '/api/order/list',
    method: 'get',
    params
  })
}

export function cancelOrder(params) {
  return request({
    url: '/api/order/cancel',
    method: 'get',
    params
  })
}
