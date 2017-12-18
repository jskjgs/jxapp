import axios from 'axios'
import {
  fetchApi,
  Cookie
} from '@/utils/index'
import { Message } from 'element-ui'
// 添加promise的finally方法
import promiseFinally from 'promise.prototype.finally'
promiseFinally.shim()

// 全局的 axios 默认transformRequest配置
// 序列化数据
axios.defaults.transformRequest = [function (data) {
  if (data instanceof FormData) {
    return data
  } else {
    let stringifyStr = ''
    for (let key in data) {
      if (data.hasOwnProperty(key)) {
        if (data[key] !== undefined) {
          stringifyStr += key + '=' + data[key] + '&'
        }
      }
    }
    return stringifyStr.slice(0, -1)
  }
}]

/* ajax拦截器 */
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
  // get请求加上timestamp
  if (config.method === 'get') {
    let delimiter = ''
    if (config.url.indexOf('?') === -1) {
      delimiter = '?'
    } else {
      delimiter = '&'
    }
    config.url += delimiter + new Date().getTime()
  }
  config.url = '/reservation/ad' + config.url
  return config
}, function (error) {
  return Promise.reject(error)
})

// 添加响应拦截器
import router from '@/router/index'
axios.interceptors.response.use((response) => {
  // 响应数据
  let data = response.data
  // 响应数据中的code
  let code = data.code
  let errMsg = ''
  switch (code) {
    case 200:
      break
    case 400:
      errMsg = '操作失败'
      break
    case 406:
      errMsg = data.message
      break
    case 401:
      errMsg = '未登陆'
      Cookie.remove('login')
      router.push('/login')
      break
    case 500:
      errMsg = '发生未知错误'
      break
    default:
      errMsg = data.message || '发生未知错误'
  }
  if (errMsg) {
    Message({
      type: 'error',
      message: errMsg
    })
    return Promise.reject(errMsg)
  } else {
    return response
  }
}, (error) => {
  Message({
    type: 'error',
    message: '网络故障'
  })
  return Promise.reject(error)
})

// 获取账号权限 (通过accountId)
export const GET_AUTH_URL = '/admin/all_permission'
export const getAuthApi = () => {
  return fetchApi({
    url: GET_AUTH_URL,
    type: 'get'
  })
}
