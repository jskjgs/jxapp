import { fetchApi } from '@/utils/index'

// 获取用户列表
export const GET_LIST_URL = '/account/queryUser'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}

// 获取个人信息
export const USER_INFO_URL = '/account/queryUserDetail'
export const userInfoApi = (accountId) => {
  return fetchApi({
    url: USER_INFO_URL,
    type: 'get',
    params: {
      accountId
    }
  })
}

// 获取用户日记
export const USER_LOG_URL = '/diary/queryByAccountId'
export const userLogApi = (params) => {
  return fetchApi({
    url: USER_LOG_URL,
    type: 'get',
    params
  })
}

// 获取关联人列表
export const RELATION_URL = '/patientInfo/queryForAdmin'
export const relationApi = (params) => {
  return fetchApi({
    url: RELATION_URL,
    type: 'get',
    params
  })
}
