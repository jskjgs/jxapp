export const STASH = 'global/stash'
export const CLEAR_STASH = 'global/clear_stash'

export const KEEPALIVE = 'global/keepAlive'
export const CLEAR_KEEPALIVE = 'global/clear_keepAlive'

export const GET_AUTH = 'global/get_auth'

export const UPDATE_ACCOUNTINFO = 'global/update_accountInfo'

import {
  getAccountInfo
} from '@/utils/index'

const state = {
  // 暂存的数据
  stash: {
    from: '', // 路由
    data: {
    }
  },
  // 保持状态的组件 (多个之间用','分隔)
  keepAlive: 'no-match',
  // todo: 删除`Array.from({length: 8 - 1 + 1}).map((item, index) => `m_0${index + 1}`) &&`
  auth: __DEV__ ? Array.from({length: 8 - 1 + 1}).map((item, index) => `m_0${index + 1}`) : (getAccountInfo() || {}).permissionList || [],
  accountInfo: getAccountInfo() || {}
}

const mutations = {
  [STASH] (state, payload) {
    state.stash = payload
  },
  [CLEAR_STASH] (state) {
    state.stash = {}
  },
  [KEEPALIVE] (state, componentName) {
    state.keepAlive = componentName
  },
  [CLEAR_KEEPALIVE] (state, componentName) {
    state.keepAlive = 'no-match'
  },
  [GET_AUTH] (state, auth) {
    state.auth = auth
  },
  [UPDATE_ACCOUNTINFO] (state, accountInfo) {
    accountInfo = accountInfo || {}
    state.accountInfo = accountInfo
    state.auth = accountInfo.permissionList || []
  }
}
const actions = {
}

export default {
  state,
  mutations,
  actions
}
