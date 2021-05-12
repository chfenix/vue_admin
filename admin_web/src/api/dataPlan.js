import request from '@/utils/request'

export function listDataPlan(data) {
  return request({
    url: '/api/dataPlan/list',
    method: 'post',
    data
  })
}

export function createDataPlan(data) {
  return request({
    url: '/api/dataPlan/create',
    method: 'post',
    data
  })
}

export function updateDataPlan(data) {
  return request({
    url: '/api/dataPlan/update',
    method: 'post',
    data
  })
}

export function deleteDataPlan(id) {
  return request({
    url: `/api/dataPlan/delete/${id}`,
    method: 'post'
  })
}
