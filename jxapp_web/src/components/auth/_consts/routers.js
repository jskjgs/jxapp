// 当前模块名（路由名前缀）
export const MOUDLE_PREDIX = 'auth'
const MOUDLE_ROOT = `/${MOUDLE_PREDIX}`

// 当前模块首页：模块入口
export const INDEX = {
  path: MOUDLE_ROOT,
  name: `${MOUDLE_PREDIX}_root`,
  meta: {
    permissionId: 'm_07',
    label: '权限管理'
  }
}
