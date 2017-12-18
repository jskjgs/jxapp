// 当前模块名（路由名前缀）
export const MOUDLE_PREDIX = 'push'
const MOUDLE_ROOT = `/${MOUDLE_PREDIX}`

// 当前模块首页：模块入口
export const INDEX = {
  path: MOUDLE_ROOT,
  name: `${MOUDLE_PREDIX}_root`,
  meta: {
    permissionId: 'm_08',
    label: '推送管理'
  }
}
