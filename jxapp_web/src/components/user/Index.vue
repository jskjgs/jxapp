<script>
  /*
   * Created by zhengji
   * Date: 2017/10/20
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'
  import {
    PERSONAL_PAGE
  } from './_consts/routers'
  import {
    getListApi
  } from './api'
  export default {
    name: 'UserManage',
    components: {
      SearchTable
    },
    data () {
      this.tableAttrs = {
        'props': {
          'tooltip-effect': 'dark',
          'style': 'width: 100%',
          'align': 'center'
        }
      }
      this.columnData = [{
        attrs: {
          'prop': 'userName',
          'label': '用户名',
          'min-width': '140'
        },
        scopedSlots: {
          default: (scope) => {
            return (
              <router-link to={{name: PERSONAL_PAGE.name, params: { accountId: scope.row.id }}}>
                {scope.row.userName}
              </router-link>
            )
          }
        }
      }, {
        attrs: {
          'prop': 'id',
          'label': '用户ID',
          'min-width': '140'
        }
      }, {
        attrs: {
          'prop': 'tel',
          'label': '手机号',
          'min-width': '100'
        }
      }, {
        attrs: {
          'label': '操作',
          'min-width': '80'
        },
        scopedSlots: {
          default: (scope) => {
            return (
              <a href={scope.row.link} target="_blank" style="color: #20a0ff;">私信</a>
            )
          }
        }
      }]
      this.listApi = {
        requestFn: getListApi,
        responseFn (res) {
          console.log(res, 'res')
          const content = res.content || {}
          const list = content.list || []
          this.tableData = list.map(item => {
            return {
              no: item.no,
              userName: item.account,
              id: item.id,
              tel: item.phone
            }
          })
          this.total = content.total || 0
        }
      }
      return {
        searchKeyword: '',
        apiKeysMap: {
          key: {
            value: undefined
          },
          currentPage: 'startPage'
        }
      }
    },
    methods: {
      handleSearch () {
        this.apiKeysMap = Object.assign({}, this.apiKeysMap, {
          key: {
            value: this.searchKeyword || undefined
          }
        })
      }
    }
  }
</script>

<template>
  <div id="user-manage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        用户管理
      </div>
    </div>
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
      <div class="table-tools flex--vcenter" slot="table-tools">
        <div class="search-wrap">
          <span class="search-label">搜索关键字：</span>
          <el-input
            class="inline-block search-input"
            placeholder="请输入客户姓名／卡号..."
            v-model="searchKeyword"
            @keyup.enter.native="handleSearch">
          </el-input>
          <el-button
            type="primary"
            style="margin-left: 30px;"
            @click="handleSearch">
            搜索
          </el-button>
        </div>
      </div>
    </search-table>
  </div>
</template>

<style lang="scss">
  #user-manage {
  }
</style>
