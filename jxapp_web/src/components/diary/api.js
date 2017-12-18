import { fetchApi } from '@/utils/index'

// 获取banner列表
export const GET_LIST_URL = '/diary/query'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}

// 上下架
export const SHELVE_URL = '/diary/show'
export const shelveApi = (id) => {
  return fetchApi({
    url: SHELVE_URL,
    type: 'post',
    params: {
      id
    }
  })
}

// 置顶
export const TOP_URL = '/diary/top'
export const topApi = (id) => {
  return fetchApi({
    url: TOP_URL,
    type: 'post',
    params: {
      id
    }
  })
}

// 审核
export const CHECK_URL = '/diary/verify'
export const checkApi = (id, status) => {
  return fetchApi({
    url: CHECK_URL,
    type: 'post',
    params: {
      id,
      status
    }
  })
}
