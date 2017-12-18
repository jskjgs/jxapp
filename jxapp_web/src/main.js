// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

// polyfill
import 'es6-symbol/implement'
import 'es6-promise/auto'

import Vue from 'vue'
import store from '@/store'
import router from '@/router'

import './rootApi'

/* 全局指令 */
import './directives/index'
/* 全局指令 END */

/* 插件 */
import { eventBus } from '@/plugins/index'
Vue.use(eventBus)

// elements组件
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
Vue.use(ElementUI)

/* 插件 END */

/* 全局mixin */
// import globalMixins from '@/mixin/global'
// globalMixins.forEach((item) => {
//   Vue.mixin(item)
// })

// 入口组件
// 注意：App必须放最后
import App from './App'

Vue.config.productionTip = false

Vue.config.errorHandler = function (err, vm) {
  console.log(err, vm)
  throw err
}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
