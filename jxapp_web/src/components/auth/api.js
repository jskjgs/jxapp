import { fetchApi } from '@/utils/index'

// 人员列表
export const GET_LIST_URL = '/admin/list'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}

// 创建管理员账号
export const CREATE_URL = '/admin/create'
export const createApi = (data) => {
  return fetchApi({
    url: CREATE_URL,
    type: 'post',
    data
  })
}

// 返回所有权限接口
export const GET_ALL_PERMISSION_URL = '/admin/all_permission'
export const getAllPermissionApi = () => {
  return fetchApi({
    url: GET_ALL_PERMISSION_URL,
    type: 'get'
  })
}

// 修改管理员账号 权限 密码
export const UPDATE_URL = '/admin/update'
export const updateApi = (data) => {
  return fetchApi({
    url: UPDATE_URL,
    type: 'post',
    data
  })
}

// 删除账号
export const DELETE_URL = '/admin/delete'
export const deleteApi = (data) => {
  return fetchApi({
    url: DELETE_URL,
    type: 'post',
    data
  })
}
