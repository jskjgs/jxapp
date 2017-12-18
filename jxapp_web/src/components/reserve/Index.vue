<script>
  /*
   * Created by zhengji
   * Date: 2017/10/21
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'
//  import { Loading } from 'element-ui'
  import HeaderSearchSelect from '@/components/_common/headerSearchSelect/HeaderSearchSelect.vue'

  import {
    getListApi as getDoctorListApi,
    queryDepartmentApi
  } from '@/components/doctor/api'
  import {
    getListApi
  } from './api'

  import {
    convertDate
  } from '@/utils/index'

  export default {
    name: 'Diary',
    components: {
      SearchTable
    },
    data () {
      const vm = this
      const statusOptions = [{
        label: '全部',
        value: undefined
      }, {
        label: '过期未到诊',
        value: 1
      }, {
        label: '正常就诊',
        value: 0
      }, {
        label: '预约就诊',
        value: 2
      }]
      const statusDict = {
        0: '正常就诊',
        1: '过期未到诊',
        2: '预约就诊'
      }
      this.tableAttrs = {
        'props': {
          'tooltip-effect': 'dark',
          'style': 'width: 100%',
          'align': 'center'
        }
      }
      this.columnData = [{
        attrs: {
          'prop': 'registerTime',
          'label': '预约时间',
          'min-width': '180',
          'formatter' (row, col) {
            return row.registerTime ? convertDate(row.registerTime, 'Y-M-D h:m') : '--'
          }
        }
      }, {
        attrs: {
          'prop': 'patientName',
          'label': '客户姓名',
          'min-width': '120'
        }
      }, {
        attrs: {
          'prop': 'idCard',
          'label': '就诊卡号',
          'min-width': '120'
        }
      }, {
        attrs: {
          'prop': 'doctorName',
          'label': '医生姓名',
          'min-width': '120',
          'render-header' (h, { column, $index }) {
            return h(HeaderSearchSelect, {
              props: {
                label: '医生姓名',
                options: vm.doctorList,
                value: vm.apiKeysMap.doctorId.value
              },
              on: {
                input (val) {
                  vm.apiKeysMap.doctorId.value = val
                },
                visibleChange (visible) {
                  if (visible) {
                    getDoctorListApi().then(res => {
                      const content = res.content || {}
                      const list = (content.list || []).map(item => {
                        const doctor = item.doctor || {}
                        return {
                          label: doctor.name,
                          value: doctor.id
                        }
                      })
                      vm.doctorList = [{
                        label: '全部',
                        value: undefined
                      }].concat(list)
                    })
                  }
                }
              }
            })
          }
        }
      }, {
        attrs: {
          'prop': 'department',
          'label': '科室',
          'min-width': '120',
          'render-header' (h, { column, $index }) {
            return h(HeaderSearchSelect, {
              props: {
                label: '科室',
                options: vm.departmentList,
                value: vm.apiKeysMap.departmentId.value
              },
              on: {
                input (val) {
                  vm.apiKeysMap.departmentId.value = val
                },
                visibleChange (visible) {
                  if (visible) {
                    if (vm.departmentList.length === 0) {
                      queryDepartmentApi().then(res => {
                        const content = (res.content || {}).map(item => {
                          return {
                            label: item.name,
                            value: item.id
                          }
                        })
                        vm.departmentList = [{
                          label: '全部',
                          value: undefined
                        }].concat(content)
                      })
                    }
                  }
                }
              }
            })
          }
        }
      }, {
        attrs: {
          'prop': 'phone',
          'label': '预留手机号',
          'min-width': '120'
        }
      }, {
        attrs: {
          'prop': 'idCard',
          'label': '身份证号',
          'min-width': '160'
        }
      }, {
        attrs: {
          'prop': 'status',
          'label': '状态',
          'min-width': '120',
          'formatter' (row, col) {
            return statusDict[row.status]
          },
          'render-header' (h, { column, $index }) {
            return (
              <el-dropdown>
                <span class="el-dropdown-link">
                  状态
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
                        class={{ 'status-active': item.value === (vm.apiKeysMap && vm.apiKeysMap.status.value) }}
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
      }]
      this.listApi = {
        requestFn: getListApi,
        responseFn (data) {
          let content = data.content || {}
          this.tableData = content.list || []
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
          key: {
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
          doctorId: {
            value: undefined
          },
          departmentId: {
            value: undefined
          },
          currentPage: 'startPage'
        },
        createTimeRange: [],
        searchKeyword: '',
        doctorList: [], // 医生列表
        departmentList: [] // 科室列表
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
          key: {
            value: this.searchKeyword || undefined
          }
        })
      }
    }
  }
</script>

<template>
  <div id="reverse-manage">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        预约管理
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
          预约时间：
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
    </search-table>
  </div>
</template>

<style lang="scss">
  #reverse-manage {
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

    .operations {
    }
  }
  .log-status-drodown-menu {
    .el-dropdown-menu__item {
      &.status-active {
        background: #20a0ff;
        color: #fff;
      }
    }
  }
</style>
