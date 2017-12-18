<script>
  import {
    getAuthApi
  } from '@/rootApi'

  import AuthTree from './AuthTree'
  export default {
    name: 'Auth',
    props: {
      accountId: [String, Number],
      // 通过companyId获取权限树（用于新建普通人员账号）
      getTreeApi: Function
    },
    components: {
      AuthTree
    },
    created () {
      this.init()
    },
    data () {
      return {
        pageLoading: false,
        activeTab: 'plan',
        treeData: []
      }
    },
    watch: {
      accountId () {
        this.init()
      }
    },
    methods: {
      init () {
        this.pageLoading = true
        if (this.getTreeApi) {
          this.getTreeApi(this.accountId).then((res) => {
            let content = res.content || []
            this.treeData = content
          }).finally(() => {
            this.pageLoading = false
          })
        } else {
          getAuthApi(this.accountId).then((res) => {
            let content = res.content || {}
            let treeData = content.permissonAndTreeDtoArrayList
            this.treeData = treeData
            let chosen = content.permissions || []
            // 选中结点不能与结点渲染处于一个队列，不然不能触发'node.checked'的监听函数
            this.$nextTick(() => {
              this.$refs.authTree.setCheckedKeys(chosen)
            })
          }).finally(() => {
            this.pageLoading = false
          })
        }
      },
      // 获取勾选的结点（包含层级关系）
      getCheckedKeys () {
        let checkedKeys = this.$refs.authTree.getCheckedKeys()
        if (checkedKeys.length === 0) {
          return []
        }
        let treeData = this.treeData
        let _treeData = JSON.parse(JSON.stringify(treeData))
        // 一级结点过滤
        for (let i = 0, len = treeData.length; i < len; i++) {
          if (checkedKeys.indexOf(_treeData[i].id) === -1) {
            _treeData.splice(i, 1)
            i--
            len--
          }
        }
        const recursive = function (item) {
          let children = item.children
          if (children && children.length > 0) {
            for (let i = 0, len = children.length; i < len; i++) {
              if (checkedKeys.indexOf(children[i].id) === -1) {
                children.splice(i, 1)
                i--
                len--
              } else {
                recursive(children[i])
              }
            }
          }
        }
        _treeData.forEach(recursive)
        return _treeData
      }
    }
  }
</script>

<template>
  <div class="auth-wrap">
    <!--<el-tabs-->
      <!--type="card"-->
      <!--v-model="activeTab">-->
      <!--<el-tab-pane label="企业定位与规划" name="plan">-->
      <!--</el-tab-pane>-->
    <!--</el-tabs>-->
    <auth-tree
      ref="authTree"
      :tree-data.sync="treeData"></auth-tree>
  </div>
</template>

<style lang="scss">
</style>
