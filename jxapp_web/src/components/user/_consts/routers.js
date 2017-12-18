// 当前模块名（路由名前缀）
export const MOUDLE_PREDIX = 'user'
const MOUDLE_ROOT = `/${MOUDLE_PREDIX}`

// 当前模块首页：模块入口
export const INDEX = {
  path: MOUDLE_ROOT,
  name: `${MOUDLE_PREDIX}_root`,
  meta: {
    permissionId: 'm_05',
    label: '用户管理'
  }
}

// 个人主页
export const PERSONAL_PAGE = {
  path: MOUDLE_ROOT + '/:accountId',
  name: `${MOUDLE_PREDIX}_personal_page`
}
