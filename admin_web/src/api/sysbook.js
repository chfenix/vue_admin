import request from '@/utils/request'

export function getSysbookList(data) {
  return request({
    url: '/api/sys/sysbook/list',
    method: 'post',
    data
  })
}

export function createSysbook(data) {
  return request({
    url: '/api/sys/sysbook/create',
    method: 'post',
    data
  })
}

export function updateSysbook(data) {
  return request({
    url: '/api/sys/sysbook/update',
    method: 'post',
    data
  })
}

export function deleteSysbook(id) {
  return request({
    url: `/api/sys/sysbook/delete/${id}`,
    method: 'delete'
  })
}
