import deepAssign from './deepAssign'
export default function (routers, permissionId) {
  return routers.map(item => {
    return deepAssign({}, item, {
      meta: {
        permissionId
      }
    })
  })
}
