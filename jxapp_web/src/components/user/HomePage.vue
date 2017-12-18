<script>
  /*
   * Created by zhengji
   * Date: 2017/10/20
   */
  import Info from './_thumbs/Info.vue'
  import Log from './_thumbs/Log.vue'
  import Relation from './_thumbs/Relation.vue'

  import {
    INDEX
  } from './_consts/routers'

  import {
    userInfoApi
  } from './api'

  export default {
    name: 'HomePage',
    components: {
      info: Info,
      log: Log,
      relation: Relation
    },
    created () {
      userInfoApi(this.accountId).then(res => {
        this.data.info = res.content || {}
      })
    },
    data () {
      this.INDEX = INDEX
      return {
        activeTab: 'info',
        data: {
          info: {},
          log: [],
          relation: []
        }
      }
    },
    computed: {
      accountId () {
        return this.$route.params.accountId
      }
    }
  }
</script>

<template>
  <div class="personal-homepage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        <router-link :to="{name: INDEX.name}">用户管理</router-link> &gt; 个人主页
      </div>
    </div>
    <div class="content-wrap flex">
      <div class="content-lt flex-item--none">
        <img
          :src="data.info.headPortrait"
          class="avatar"
          width="80"
          height="80"
          alt=""/>
        <p class="text-center">{{ data.info.nick }}</p>
        <el-button type="primary" size="small" style="width: 100%;">发私信</el-button>
      </div>
      <div class="content-rt flex-item">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="个人信息" name="info"></el-tab-pane>
          <el-tab-pane label="日记" name="log"></el-tab-pane>
          <el-tab-pane label="关联就诊人" name="relation"></el-tab-pane>
        </el-tabs>
        <div class="content-body">
          <keep-alive>
            <component 
              :is="activeTab"
              :data="data[activeTab]">
            </component>
          </keep-alive>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  .personal-homepage {
    .content-wrap {
      margin-top: 40px;
    }
    .content-lt {
      .avatar {
        display: inline-block;
        background: #d8d8d8;
      }
    }
    .content-rt {
      margin-left: 30px;
      border: 1px solid #d3dce6;
      min-height: 580px;

      .el-tabs__item {
        width: 120px;
        text-align: center;
      }

      .content-body {
        padding: 30px;
      }
    }
  }
</style>
