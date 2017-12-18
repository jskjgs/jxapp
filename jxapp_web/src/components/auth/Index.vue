<script>
/*
 * Created by zhengji
 * Date: 2017/9/11
 */
import EditDialog from './_thumbs/EditDialog.vue'
import SearchTable from '@/components/_common/searchTable/SearchTable'
import { Loading } from 'element-ui'
import {
  getListApi,
  createApi,
  updateApi,
  deleteApi
} from './api'
let adding = false

export default {
  name: 'Auth',
  components: {
    EditDialog,
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
        'prop': 'account',
        'label': '用户名',
        'width': '120'
      }
    }, {
      attrs: {
        'prop': 'auth',
        'label': '权限',
        'min-width': '200'
      }
    }, {
      slotName: 'column-operate'
    }]
    this.listApi = {
      requestFn: getListApi,
      responseFn (data) {
        let content = data.content || {}
        this.tableData = (content.list || []).map((item) => ({
          id: item.id,
          account: item.account,
          auth: item.permissionList.map(item => item.name).join('/'),
          permissionList: item.permissionList,
          psd: item.password
        }))
        this.total = content.total || 0
      }
    }
    this.apiKeysMap = {
      currentPage: 'pageNum'
    }
    return {
      apiKeysMap: {
        currentPage: 'pageNum'
      },
      editDialogVisible: false,
      editData: null
    }
  },
  watch: {
    editDialogVisible (val) {
      if (!val) {
        this.editData = null
        adding = false
      }
    },
    currentPage (newPageNum) {
      this.getList({
        pageNum: newPageNum
      })
    }
  },
  methods: {
    getList (params) {
      this.tableLoading = true
    },
    // 刷新列表
    refreshList () {
      this.apiKeysMap = Object.assign({}, this.apiKeysMap)
    },
    // 编辑或新增
    openEditDialog (rowData, isAdd) {
      this.editDialogVisible = true
      this.editData = rowData
      adding = !!isAdd
    },
    // 提交编辑或新增
    handleEditSubmit (data, respondCb) {
      const successCb = (msg) => {
        this.$message({
          type: 'success',
          message: msg
        })
        respondCb(true)
        this.apiKeysMap = Object.assign({}, this.apiKeysMap)
      }
      if (adding) {
        createApi({
          account: data.name,
          password: data.psd,
          permission: JSON.stringify(data.pickedAuth)
        }).then(() => {
          successCb('新建成功')
        })
      } else {
        updateApi({
          id: data.id,
          permission: JSON.stringify(data.pickedAuth),
          password: data.psd
        }).then(() => {
          successCb('修改成功')
        })
      }
      console.log(data, respondCb, 'respondCb')
    },
    handleEditCancel () {
      console.log('adding', adding)
    },
    delRow (rowData) {
      this.$confirm(`是否删除该日志？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            const loading = Loading.service({ fullscreen: true })
            deleteApi({
              id: rowData.id
            }).then(res => {
              this.$message({
                type: 'success',
                message: `删除成功`
              })
              this.refreshList()
            }).finally(() => {
              loading.close()
              done()
            })
          } else {
            done()
          }
        }
      })
    }
  }
}
</script>

<template>
  <div class="auth-manage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        权限管理
      </div>
    </div>
    <div class="table-tools flex--vcenter" v-if="$store.state.accountInfo.account === 'admin'">
      <div class="btn-wrap">
        <el-button
          class="btn--add"
          type="primary"
          @click="openEditDialog(null, true)">
          新增 <i class="el-icon-plus"></i>
        </el-button>
      </div>
    </div>
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
      <template slot="column-operate" v-if="$store.state.accountInfo.account === 'admin'">
        <el-table-column
          width="140"
          label="操作">
          <template scope="scope">
            <div class="flex--center operations" v-if="scope.row.account !== 'admin'">
              <span
                class="operate-item el-icon-edit"
                @click="openEditDialog(scope.row)">
              </span>
              <span
                class="operate-item el-icon-delete"
                @click="delRow(scope.row)">
              </span>
            </div>
          </template>
        </el-table-column>
      </template>
    </search-table>
    <edit-dialog
      v-model="editDialogVisible"
      :data="editData"
      @cancel="handleEditCancel"
      @submit="handleEditSubmit">
    </edit-dialog>
  </div>
</template>

<style lang="scss">
  @import "~@/assets/style/variables/index";

  .auth-manage {
    .table-tools {
      margin-top: 20px;
      justify-content: flex-end;

      .el-button {
        border-radius: 18px;
      }
    }

    .el-table {
      margin-top: 20px;
      th, td {
        text-align: center;
      }

      .operate-item {
        color: $color4;
        font-size: 18px;
        cursor: pointer;
        & + .operate-item {
          margin-left: 20px;
        }
      }
    }

    .pagination-wrap {
      margin-top: 30px;
      text-align: right;
    }
  }
</style>
