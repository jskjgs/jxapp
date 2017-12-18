<script>
  /*
   * Created by zhengji
   * Date: 2017/10/21
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'
  import EditDialog from './_thumbs/EditDialog.vue'

  import { Loading } from 'element-ui'

  import {
    // getListApi,
    shelveApi,
    topApi,
    checkApi
  } from '../diary/api'
  import {
    queryNotifyApi
  } from './api'

  import {
    convertDate
  } from '@/utils/index'

  let adding = false
  export default {
    name: 'Diary',
    components: {
      SearchTable,
      EditDialog
    },
    data () {
      let vm = this
      const statusOptions = [{
        label: '全部',
        value: undefined
      }, {
        label: '直接推送',
        value: 1
      }, {
        label: '定时推送',
        value: 0
      }]
      this.tableAttrs = {
        'props': {
          'tooltip-effect': 'dark',
          'style': 'width: 100%',
          'align': 'center'
        }
      }
      this.columnData = [{
        attrs: {
          'prop': 'pushTime',
          'label': '发送时间',
          'min-width': '150',
          'formatter' (row, col) {
            return row.estTime ? convertDate(row.estTime, 'Y-M-D h:m:s') : '--'
          }
        }
      }, {
        attrs: {
          'prop': 'content',
          'label': '内容',
          'min-width': '180'
        },
        'scopedSlots': {
          default: (scope) => {
            return (
              <a href={scope.row.url} target="_blank">{ scope.row.title }</a>
            )
          }
        }
      }, {
        attrs: {
          'prop': 'target',
          'label': '目标人群',
          'min-width': '100'
        }
      }, {
        attrs: {
          'prop': 'type',
          'label': '类型',
          'render-header' (h, { column, $index }) {
            return (
              <el-dropdown>
                <span class="el-dropdown-link">
                  类型
                  <i
                    class="el-icon-arrow-down el-icon--right"
                    style="cursor: pointer;">
                  </i>
                </span>
                <el-dropdown-menu slot="dropdown" class="push-type-drodown-menu">
                  {
                    statusOptions.map(item => {
                      return (
                        <el-dropdown-item
                          class={{ 'active': item.value === (vm.apiKeysMap && vm.apiKeysMap.status.value) }}
                          nativeOnClick={() => vm.selectStatus(item)}>
                          { item.label }
                        </el-dropdown-item>
                      )
                    })
                  }
                </el-dropdown-menu>
              </el-dropdown>
            )
          }
        }
      }, {
        attrs: {
          'label': '操作',
          'width': 280
        },
        scopedSlots: {
          default: (scope) => {
            return (
              <div class="flex--center operations">
                <span
                  class="operate-item"
                  style="color: #20a0ff;"
                  onClick={() => this.openEditDialog(scope.row)}>
                  编辑
                </span>
                <span
                  class="operate-item"
                  style="color: #20a0ff;"
                  onClick={() => this.handleCheck(scope.row)}>
                  转发
                </span>
                <span
                  class="operate-item el-icon-delete"
                  onClick={() => this.handleDelRow(scope.row)}>
                </span>
              </div>
            )
          }
        }
      }]
      this.listApi = {
        requestFn: queryNotifyApi,
        responseFn (data) {
          let content = data.content || {}
          this.tableData = (content.list || []).map((item) => ({
            estTime: item.createTime,
            accountId: item.accountId,
            id: item.id,
            title: item.title,
            nick: item.nick,
            status: item.status,
            isTop: item.isTop,
            url: item.url,
            enable: item.enable // 0表示正常
          }))
          console.log('this.tableData', this.tableData)
          this.total = content.total || 0
        }
      }
      this.checkOptions = [{
        label: '审核通过',
        value: '0'
      }, {
        label: '审核拒绝',
        value: '2'
      }]
      return {
        apiKeysMap: {
          query: {
            value: ''
          },
          status: {
            value: undefined
          },
          startTime: {
            value: ''
          },
          endTime: {
            value: ''
          },
          orderBy: {
            value: 'create_time'
          },
          desc: {
            value: true
          },
          currentPage: 'pageNum'
        },
        createTimeRange: '',
        searchKeyword: '',
        editDialogVisible: false // 编辑弹框
      }
    },
    watch: {
      checkDialogVisible (visible) {
        if (!visible) {
          this.checkStatus = ''
        }
      }
    },
    methods: {
      selectStatus (status) {
        this.apiKeysMap.status.value = status.value
      },
      handleSearch () {
        const createTimeRange = this.createTimeRange || []
        this.apiKeysMap = Object.assign({}, this.apiKeysMap, {
          startTime: {
            value: new Date(createTimeRange[0] || '').getTime() || undefined
          },
          endTime: {
            value: new Date(createTimeRange[1] || '').getTime() || undefined
          },
          query: {
            value: this.searchKeyword || undefined
          }
        })
      },
      // 切换置顶状态
      switchTop (rowData) {
        topApi(rowData.id).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功'
          })
        }).finally(() => {
          this.$refs.searchTable.init()
        })
      },
      // 审核（打开审核弹窗）
      handleCheck (rowData) {
        this.onRowData = rowData // 暂存当前行的数据
        this.checkDialogVisible = true
      },
      // 审核（提交）
      handleCheckSubmit () {
        checkApi(this.onRowData.id, this.checkStatus).then(res => {
          this.$message({
            type: 'success',
            message: '审核成功'
          })
          this.$refs.searchTable.getList()
        }).finally(() => {
          this.checkDialogVisible = false
        })
      },
      // 上下架
      handleUnShelve (rowData) {
        const enable = rowData.enable
        const operateText = enable + '' === '0' ? '下架' : '上架'
        this.$confirm(`是否${operateText}该日志？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              const loading = Loading.service({ fullscreen: true })
              shelveApi(rowData.id).then(res => {
                this.$message({
                  type: 'success',
                  message: `${operateText}成功`
                })
                this.$refs.searchTable.getList()
              }).finally(() => {
                loading.close()
                done()
              })
            } else {
              done()
            }
          }
        })
      },
      // 编辑
      openEditDialog (rowData, isAdd) {
        this.editDialogVisible = true
        this.editData = rowData
        adding = !!isAdd
        console.log(adding)
      },
      // 删除
      handleDelRow (rowData) {
        this.$confirm('是否删除该信息？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              Promise.resolve().then(res => {
                done()
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
                this.$refs.searchTable.getList()
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
  <div id="push-manage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        推送管理
      </div>
    </div>
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
      <div class="table-tools flex--vcenter" slot="table-tools">
        <div class="table-tools__left flex--vcenter">
          <div class="tool-item">
            发送时间：
            <el-date-picker
              v-model="createTimeRange"
              type="daterange"
              style="width: 230px;"
              placeholder="选择日期范围">
            </el-date-picker>
          </div>
          <div class="tool-item">
            搜索关键字：
            <el-input
              v-model="searchKeyword"
              placeholder="请输入客户姓名／卡号..."
              style="width: 290px;">
            </el-input>
          </div>
          <div class="tool-item">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </div>
        </div>
        <div class="table-tools__right">
          <el-button
            class="btn--add"
            type="primary"
            @click="openEditDialog(null, true)">
            新增 <i class="el-icon-plus"></i>
          </el-button>
        </div>
      </div>
    </search-table>
    <edit-dialog
      v-model="editDialogVisible">
    </edit-dialog>
  </div>
</template>

<style lang="scss">
  #push-manage {
    .table-tools {
      justify-content: space-between;
    }
  }
</style>
