import axios from 'axios'
import Qs from 'qs'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

if (process.env.NODE_ENV === 'development') {
  service.defaults.baseURL = 'http://localhost:8081/'
  service.defaults.withCredentials = true
}

// request interceptor
service.interceptors.request.use(
  config => {
    if (getToken() !== '') {
      config.headers['Authorization'] = getToken()
    }
    const method = config.method.toLocaleLowerCase()
    if (method === 'get') {
      // GET请求就序列化参数
      if (config.data !== undefined && config.data !== null) {
        config.url += ('?' + Qs.stringify(config.data))
      }
    }
    return config
  },
  error => {
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 0000, it is judged as an error.
    if (res.code !== 0) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 9996:无效TOKEN  9997:TOKEN已过期   1003:已经在其他客户端登录
      if (res.code === 9996 || res.code === 9997 || res.code === 1003) {
        // to re-login
        MessageBox.confirm('由于长时间未操作，您的登录信息已失效，点击取消停留在当前页面或重新登录', '确认登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
