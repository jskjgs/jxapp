import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
import NAVS from '@/components/_common/leftNav/NAVS'
import $store from '@/store'

/* 各模块路由配置项 */
// 引入所有模块的router.js配置文件
function importAllRouter (r) {
  return r.keys().reduce((prev, curr, index) => {
    return prev.concat(r(curr).default.concat())
  }, [])
}
const allRouters = importAllRouter(require.context('@/components/', true, /router\.js/))

// 登陆
const Login = resolve => require(['@/components/login/Login'], resolve)
// 404
const NotFound = resolve => require(['@/components/notFound/NotFound'], resolve)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Root'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    ...allRouters,
    {
      path: '*',
      name: 'NotFound',
      component: NotFound
    }
  ]
})

// 全局路由验证登陆状态
import { Cookie } from '@/utils/index'
router.beforeEach((to, from, next) => {
  const MY_AUTH = $store.state.auth || [] // 登陆人拥有的权限
  if (to.name !== 'Login' && !Cookie.get('login')) {
    next({name: 'Login'})
  } else if (to.name === 'Login' && Cookie.get('login') === 'yes') {
    next(from.path || '/')
  } else if (to.path === '/') {
    let redirectPath
    NAVS.find(function fn (nav) {
      if (nav.children) {
        nav.children.find(fn)
      } else {
        const matched = MY_AUTH.indexOf(nav.permissionId) !== -1
        if (matched && !redirectPath) {
          redirectPath = nav.path
        }
        return matched
      }
    })
    next(redirectPath)
  } else {
    if (to.name === 'Login' || to.name === 'NotFound') {
      next()
    } else {
      if (MY_AUTH.indexOf(to.meta.permissionId) !== -1) { // 校验权限
        next()
      }
    }
  }
})

export default router
