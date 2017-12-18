// 当前模块名（路由名前缀）
export const MOUDLE_PREDIX = 'reserve'
const MOUDLE_ROOT = `/${MOUDLE_PREDIX}`

// 当前模块首页：模块入口
export const INDEX = {
  path: MOUDLE_ROOT,
  name: `${MOUDLE_PREDIX}_root`,
  meta: {
    permissionId: 'm_03',
    label: '预约管理'
  }
}
