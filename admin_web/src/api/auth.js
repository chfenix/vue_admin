import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}

export function getInfo() {
  return request({
    url: '/api/auth/info',
    method: 'get'
  })
}

export function changePwd(data) {
  return request({
    url: '/api/auth/changePwd',
    method: 'post',
    data
  })
}

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
