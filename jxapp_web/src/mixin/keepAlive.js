import {
  mapMutations
} from 'vuex'
import {
  KEEPALIVE
} from '@/store/global'

/**
 *
 * @param fromRouters     指定的切入当前页面不刷新的上一个路由数组
 * @param initFnName      初始页面方法名（挂载在vm上的方法）
 * @returns { options }
 */
export default function (fromRouters, initFnName) {
  // 防止beforeRouteEnter和created中的init方法重复执行
  let __initing = false
  return {
    beforeRouteEnter (to, from, next) {
      // 非指定路由切换回来，则重置页面
      if (fromRouters.indexOf(from.name) === -1) {
        __initing = true
        next((vm) => {
          vm[initFnName || 'init']()
          __initing = false
        })
      } else {
        next()
      }
    },
    created () {
      if (!__initing) {
        this[initFnName || 'init']()
        __initing = false
      }
    },
    methods: {
      ...mapMutations({
        // 缓存页面组件状态
        keepAlive: KEEPALIVE
      })
    }
  }
}
