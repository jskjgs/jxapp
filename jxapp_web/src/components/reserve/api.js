import { fetchApi } from '@/utils/index'

// 获取列表
export const GET_LIST_URL = '/register/queryRegisterAdmin'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}
