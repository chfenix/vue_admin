import request from '@/utils/request'

export function getUserList(data) {
  return request({
    url: '/api/sys/user/list',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/api/sys/user/create',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api/sys/user/update',
    method: 'post',
    data
  })
}

export function invalidUser(id) {
  return request({
    url: `/api/sys/user/invalid/${id}`,
    method: 'get'
  })
}

export function getAllRole() {
  return request({
    url: `/api/sys/role/all`,
    method: 'get'
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/api/sys/user/userRoles/${userId}`,
    method: 'get'
  })
}
