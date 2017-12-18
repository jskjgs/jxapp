import { fetchApi } from '@/utils/index'

// 获取项目列表

// 获取科室列表
export const QUERY_DEPARTMENT_URL = '/department/queryAllDepartment'
export const queryDepartmentApi = (params) => {
  return fetchApi({
    url: QUERY_DEPARTMENT_URL,
    type: 'get',
    params
  })
}

// 获取医生列表
export const GET_LIST_URL = '/doctor_i/queryAllDoctor'
export const getListApi = (params) => {
  return fetchApi({
    url: GET_LIST_URL,
    type: 'get',
    params
  })
}

// 修改医生信息
export const MODIFY_DOCTOR_URL = '/doctor_i/modifyDoctor'
export const modifyDoctorApi = (params, data) => {
  return fetchApi({
    url: MODIFY_DOCTOR_URL,
    type: 'post',
    params,
    data
  })
}

// 置顶医生
export const TOP_DOCTOR_URL = '/doctor_i/topDoctor'
export const topDoctorApi = (data) => {
  return fetchApi({
    url: TOP_DOCTOR_URL,
    type: 'post',
    data
  })
}
