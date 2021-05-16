import request from '@/utils/request'

export function getRoleList(data) {
  return request({
    url: '/api/sys/role/list',
    method: 'post',
    data
  })
}

export function createRole(data) {
  return request({
    url: '/api/sys/role/create',
    method: 'post',
    data
  })
}

export function getMenuTree() {
  return request({
    url: '/api/sys/role/menuTree',
    method: 'get'
  })
}

export function getRoleMenus(id) {
  return request({
    url: `/api/sys/role/roleMenus/${id}`,
    method: 'get'
  })
}

export function updateRole(data) {
  return request({
    url: '/api/sys/role/update',
    method: 'post',
    data
  })
}

export function invalidRole(id) {
  return request({
    url: `/api/sys/role/invalid/${id}`,
    method: 'get'
  })
}
