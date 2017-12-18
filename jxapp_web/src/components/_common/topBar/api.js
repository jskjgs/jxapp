import { fetchApi } from '@/utils/index'

// 退出登录
export const LOG_OUT_URL = '/admin/logout'
export const logoutApi = () => {
  return fetchApi({
    url: LOG_OUT_URL,
    type: 'post'
  })
}
