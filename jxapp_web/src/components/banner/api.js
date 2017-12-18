import { fetchApi } from '@/utils/index'

// 获取banner列表
export const GET_LIST_URL = '/home/queryAllBanner'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}

// 修改banner
export const MODIFY_BANNER_URL = '/home/modifyBanner'
export const modifyBannerApi = (params, data) => {
  return fetchApi({
    url: MODIFY_BANNER_URL,
    type: 'post',
    params,
    data
  })
}

// 删除banner (单个)
export const DELETE_BANNER_URL = '/home/deleteBanner'
export const deleteBannerApi = (params) => {
  return fetchApi({
    url: DELETE_BANNER_URL,
    type: 'delete',
    params
  })
}

// 删除banner (批量)
export const DELETE_BANNER_BATCH_URL = '/home/deleteBannerBatch'
export const deleteBannerBatchApi = (params) => {
  return fetchApi({
    url: DELETE_BANNER_BATCH_URL,
    type: 'delete',
    params
  })
}

// 修改展示数量 (未完成)
export const MODIFY_DISPLAY_URL = '/home/deleteBanner'
export const modifyDisplayApi = (data) => {
  return fetchApi({
    url: MODIFY_DISPLAY_URL,
    type: 'post',
    data
  })
}

// 切换banner状态
export const SWITCH_VISIBLE_URL = '/home/hideOrShowBanner'
export const switchVisibleApi = (data) => {
  return fetchApi({
    url: SWITCH_VISIBLE_URL,
    type: 'post',
    data
  })
}

// 增加banner
export const ADD_BANNER_URL = '/home/addBanner'
export const addBanenrApi = (params, data) => {
  return fetchApi({
    url: ADD_BANNER_URL,
    type: 'put',
    params,
    data
  })
}
