import request from '@/utils/request'

export function searchUser(name) {
  return request({
    url: '/api/user/remoteSearch',
    method: 'get',
    params: { name }
  })
}

export function searchDataplan(name) {
  return request({
    url: '/api/dataPlan/remoteSearch',
    method: 'get',
    params: { name }
  })
}
