<script>
  // 顶部bar
  import TopBar from '@/components/_common/topBar/TopBar'
  // 左侧导航
  import LeftNav from '@/components/_common/leftNav/LeftNav'

  export default {
    name: 'App',
    components: {
      TopBar,
      LeftNav
    },
    data () {
      return {
      }
    },
    computed: {
      showFixedBar () {
        let route = this.$route
        return route.path !== '/' && route.name !== 'Login' && route.name !== 'NotFound'
      }
    }
  }
</script>

<template>
  <div id="app">
    <template v-if="showFixedBar">
      <div class="flex body-wrap">
        <left-nav class="flex-item--none"></left-nav>
        <div id="page-content" class="flex-item">
          <top-bar></top-bar>
          <keep-alive :include="$store.state.keepAlive">
            <router-view></router-view>
          </keep-alive>
        </div>
      </div>
    </template>

    <template v-else>
      <router-view></router-view>
    </template>
  </div>
</template>

<style lang="scss">
  /* 全局样式 */
  @import '~@/assets/style/index';
  @import "~@/assets/style/variables/index";

  #app {
    min-width: 1200px;
    height: calc(100vh - #{ $scollbar_w });

    .body-wrap {
      height: 100%;

      @at-root {
        #page-content {
          overflow-y: auto;
          padding: 30px;
          height: 100%;
        }
      }
    }
  }
</style>
