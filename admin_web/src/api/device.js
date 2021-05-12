import request from '@/utils/request'

export function bindDevice(data) {
  return request({
    url: '/api/device/bind',
    method: 'post',
    data
  })
}

export function getList(params) {
  return request({
    url: '/api/device/list',
    method: 'get',
    params
  })
}

export function unbindDevice(params) {
  return request({
    url: '/api/device/unbind',
    method: 'get',
    params
  })
}

export function listDeviceInfo(data) {
  return request({
    url: '/api/device/list',
    method: 'post',
    data
  })
}

export function createDeviceInfo(data) {
  return request({
    url: '/api/device/create',
    method: 'post',
    data
  })
}

export function updateDeviceInfo(data) {
  return request({
    url: '/api/device/update',
    method: 'post',
    data
  })
}

export function deleteDeviceInfo(id) {
  return request({
    url: `/api/device/delete/${id}`,
    method: 'post'
  })
}
