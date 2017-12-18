<script>
  /*
   * Created by zhengji
   * Date: 2017/10/21
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'
  import { Loading } from 'element-ui'
  import {
    convertDate
  } from '@/utils/index'

  import {
    shelveApi,
    topApi,
    checkApi
  } from '../../diary/api'
  import {
    userLogApi
  } from '../api'

  export default {
    name: 'Log',
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
          'prop': 'createTime',
          'label': '创建时间',
          'min-width': '80',
          'formatter' (row, col) {
            return row.createTime ? convertDate(row.createTime) : '--'
          }
        }
      }, {
        attrs: {
          'prop': 'title',
          'label': '日记标题',
          'min-width': '140'
        },
        'scopedSlots': {
          default: (scope) => {
            return (
              scope.row.title ? (<a href={scope.row.url} target="_blank">{ decodeURIComponent(scope.row.title) }</a>)
              : '--'
            )
          }
        }
      }, {
        attrs: {
          'prop': 'status',
          'label': '审批状态',
          'min-width': '140'
        },
        'scopedSlots': {
          default: (scope) => {
            const statusMap = (status) => {
              const DICT = {
                0: {
                  text: '审核通过',
                  class: 'status-pass'
                },
                1: {
                  text: '待审批',
                  class: 'status-wait'
                },
                2: {
                  text: '审核拒绝',
                  class: 'status-refuse'
                }
              }
              return DICT[status]
            }
            const statusInfo = statusMap(scope.row.status)
            return (
              <span class={['status-label', statusInfo.class]}>{ statusInfo.text }</span>
            )
          }
        }
      }, {
        attrs: {
          'label': '操作',
          'min-width': '180'
        },
        scopedSlots: {
          default: (scope) => {
            return (
              <div class="flex--vcenter operations">
                <span class="operate-item flex--vcenter">
                  <el-switch
                    style="margin-right: 10px;"
                    value={scope.row.isTop}
                    onChange={() => this.switchTop(scope.row)}
                    {...{props: { 'on-text': '', 'off-text': '' }}}>
                  </el-switch>
                  { scope.row.top === 1 ? '置顶' : '取消置顶' }
                </span>
                <el-button
                  type="text"
                  class="operate-item"
                  disabled={scope.row.status + '' !== '1'}
                  onClick={() => this.handleCheck(scope.row)}>
                  审核
                </el-button>
                <el-button
                  type="text"
                  class="operate-item"
                  disabled={scope.row.status + '' !== '0'}
                  onClick={() => this.handleUnShelve(scope.row)}>
                  { scope.row.status + '' === '0' && scope.row.enable + '' !== '0' ? '上架' : '下架' }
                </el-button>
              </div>
            )
          }
        }
      }]
      this.listApi = {
        requestFn: userLogApi,
        responseFn (data) {
          let content = data.content || {}
          this.tableData = (content.list || []).map((item) => ({
            id: item.id,
            createTime: item.createTime,
            title: item.title,
            status: item.status
          }))
          this.total = content.total || 0
        }
      }
      // 审核可选项
      this.checkOptions = [{
        label: '审核通过',
        value: '0'
      }, {
        label: '审核拒绝',
        value: '2'
      }]
      return {
        apiKeysMap: {
          accountId: {
            value: this.$route.params.accountId
          },
          currentPage: 'startPage'
        },
        checkDialogVisible: false, // 审核弹窗
        checkStatus: '' // 当前选择的审核状态
      }
    },
    methods: {
      // （取消）置顶
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
      }
    }
  }
</script>

<template>
  <div class="user-logs">
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
    </search-table>
    <el-dialog
      title="审核"
      :visible.sync="checkDialogVisible"
      size="tiny">
      <div style="text-align: center;height: 100px;">
        <span>审核操作：</span>
        <el-select v-model="checkStatus" placeholder="请选择">
          <el-option
            v-for="item in checkOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="checkDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCheckSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.user-logs {
  .status-label {
    display: inline-block;
    width: 60px;
    height: 24px;
    border-radius: 4px;
    font-size: 12px;

    &.status-pass {
      color: #10ad57;
      background: rgba(19,206,102,0.10);
      border: 1px solid rgba(19,206,102,0.20);
    }
    &.status-wait {
      color: #20a0ff;
      background: rgba(32,160,255,0.10);
      border: 1px solid rgba(32,160,255,0.20);
    }
    &.status-refuse {
      color: #ff4949;
      background: rgba(255,73,73,0.10);
      border: 1px solid rgba(255,73,73,0.20);
    }
  }
}
</style>
