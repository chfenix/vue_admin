import request from '@/utils/request'

export function getCustomerList(data) {
  return request({
    url: '/api/user/list',
    method: 'post',
    data
  })
}

export function createCustomer(data) {
  return request({
    url: '/api/user/create',
    method: 'post',
    data
  })
}

export function updateCustomer(data) {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
}

export function invalidCustomer(id) {
  return request({
    url: `/api/user/invalid/${id}`,
    method: 'post'
  })
}
