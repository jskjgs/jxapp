<script>
  /*
   * Created by zhengji
   * Date: 2017/10/6
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'

  import { Loading } from 'element-ui'

  import {
    getListApi,
    shelveApi,
    topApi,
    checkApi
  } from './api'

  import {
    convertDate
  } from '@/utils/index'

  export default {
    name: 'LogList',
    components: {
      SearchTable
    },
    data () {
      let vm = this
      // 状态筛选项
      const statusOptions = [{
        label: '全部',
        value: undefined
      }, {
        label: '待审核',
        value: 1
      }, {
        label: '审核通过',
        value: 0
      }, {
        label: '审核拒绝',
        value: 2
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
          'prop': 'estTime',
          'label': '创建时间',
          'min-width': '100',
          'formatter' (row, col) {
            return row.estTime ? convertDate(row.estTime) : '--'
          }
        }
      }, {
        attrs: {
          'prop': 'title',
          'label': '日记标题',
          'min-width': '120'
        },
        'scopedSlots': {
          default: (scope) => {
            return (
              <a href={scope.row.url} target="_blank">{ scope.row.title || '--' }</a>
            )
          }
        }
      }, {
        attrs: {
          'prop': 'nick',
          'label': '用户昵称',
          'min-width': '80'
        }
      }, {
        attrs: {
          'prop': 'accountId',
          'label': '用户ID',
          'min-width': '80'
        }
      }, {
        attrs: {
          'prop': 'status',
          'render-header' (h, { column, $index }) {
            return (
              <el-dropdown>
                <span class="el-dropdown-link">
                  审批状态
                  <i
                    class="el-icon-arrow-down el-icon--right"
                    style="cursor: pointer;">
                  </i>
                </span>
                <el-dropdown-menu slot="dropdown" class="log-status-drodown-menu">
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
          'width': 280
        },
        scopedSlots: {
          default: (scope) => {
            return (
              <div class="flex--vcenter operations">
                <span class="operate-item flex--vcenter" style="width: 110px;">
                  <el-switch
                    style="margin-right: 10px;"
                    value={scope.row.isTop}
                    onInput={(isTop) => (scope.row.isTop = isTop)}
                    onChange={() => this.switchTop(scope.row)}
                    {...{props: { 'on-text': '', 'off-text': '' }}}>
                  </el-switch>
                  { scope.row.isTop ? '置顶' : '取消置顶' }
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
      // 列表请求配置
      this.listApi = {
        requestFn: getListApi,
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
          query: {
            value: undefined
          },
          status: {
            value: undefined
          },
          startTime: {
            value: undefined
          },
          endTime: {
            value: undefined
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
        checkDialogVisible: false, // 审核弹窗
        checkStatus: '' // 当前选择的审核状态
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
      }
    }
  }
</script>

<template>
  <div id="log-manage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        日记管理
      </div>
    </div>
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
      <div class="table-tools flex--vcenter" slot="table-tools">
        <div class="tool-item">
          创建时间：
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
            placeholder="请输入ID号码 / 作者昵称 / 日记标题"
            style="width: 290px;">
          </el-input>
        </div>
        <div class="tool-item">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </div>
      </div>
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

<style lang="scss">
  #log-manage {
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
