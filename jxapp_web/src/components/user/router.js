import injectPermissionId from '@/utils/injectPermissionId'
import {
  INDEX,
  PERSONAL_PAGE
} from './_consts/routers'
// 该模块的权限ID
const permissionId = INDEX.meta.permissionId

export default injectPermissionId([
  Object.assign({}, INDEX, {
    component: resolve => require(['./Index'], resolve)
  }),
  Object.assign({}, PERSONAL_PAGE, {
    component: resolve => require(['./HomePage'], resolve)
  })
], permissionId)
