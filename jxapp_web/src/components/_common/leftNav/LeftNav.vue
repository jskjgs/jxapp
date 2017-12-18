<script>
  // 一级导航
  import NAVS from './NAVS'

  import {
    mapState
  } from 'vuex'

  export default {
    name: 'LeftNav',
    data () {
      return {
      }
    },
    created () {
      console.log(this.$route.path.match(/\/\w+((?=\/)|$)/)[0])
      this.NAVS = NAVS
    },
    computed: {
      ...mapState({
        myAuth: 'auth'
      })
    },
    methods: {
      handleSelect (index) {
        if (!this.activePath || this.activePath !== index) {
          this.activePath = index
          this.$refs.elMenu.openedMenus = [] // 关闭打开的子菜单
        }
      }
    }
  }
</script>

<template>
  <div id="left-nav">
    <el-menu
      theme="dark"
      ref="elMenu"
      :unique-opened="true"
      :default-active="$route.path.match(/\/\w+((?=\/)|$)/)[0]"
      @select="handleSelect"
      router>
      <el-menu-item
        class="menu-index"
        index="user">
        <span style="font-size: 18px;">后台管理系统</span>
      </el-menu-item>
      <template v-for="nav in NAVS">
        <el-submenu
          v-if="nav.children && nav.children.find(subNav => myAuth.indexOf(subNav.permissionId) !== -1)"
          :key="nav.label"
          index="nav">
          <template slot="title">
            <i v-if="nav.icon" class="nav-icon" :class="nav.icon"></i>{{ nav.label }}
          </template>
          <template v-for="subNav in nav.children">
            <el-menu-item
              v-if="myAuth.indexOf(subNav.permissionId) !== -1"
              :key="subNav.label"
              :index="subNav.path">
              {{ subNav.label }}
            </el-menu-item>
          </template>
        </el-submenu>
        <el-menu-item
          v-else-if="myAuth.indexOf(nav.permissionId) !== -1"
          :index="nav.path">
          <i v-if="nav.icon" class="nav-icon" :class="nav.icon"></i>{{ nav.label }}
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<style lang="scss">
  @import '~@/assets/style/variables/index.scss';

  #left-nav {
    overflow-y: auto;
    border-radius: 2px;
    width: 200px;
    height: 100%;

    background: $bg1;

    .menu-index {
      height: 72px;
      line-height: 72px;
    }
    .el-menu--dark {
      background: transparent;

      .el-submenu .el-menu {
        background: $bg2;
      }
      .el-menu-item, .el-submenu__title {
        color: $color1;

        &.is-active {
          background: $bg4;
          color: #fff;
        }
      }
    }

    .nav-icon {
      display: inline-block;
      transform: scale(1.4);
      margin-right: 6px;
    }
  }
</style>
