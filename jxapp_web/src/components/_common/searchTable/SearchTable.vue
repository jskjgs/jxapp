<script>
  export default {
    name: 'SearchTable',
    props: {
      initData: {
        type: Array
      },
      listApi: { // 查询列表的api方法：包括请求和响应
        type: Object,
        default () {
          return {
            requestFn () {
              return Promise.reject()
            },
            responseFn () {}
          }
        }
      },
      apiKeysMap: { // 参数字段映射：当前组件 => api参数
        type: Object
      },
      tableAttrs: { // el-table组件的props
        type: Object
      },
      columnData: { // el-table-column组件的配置参数
        type: Array,
        default: () => []
      }
    },
    created () {
      if (!this.initData) { // 传入表格数据的表格不需要请求后台
        this.init()
      }
    },
    data () {
      return {
        loading: false,
        currentPage: 1, // 默认映射到后台api的参数为：pageNum
        pageSize: 10,
        total: 0,
        tableData: []
      }
    },
    watch: {
      currentPage (newPageNum) {
        console.log('newPageNum', newPageNum)
        this.getList({
          [this.apiKeys.currentPage]: newPageNum
        })
      },
      listQueryParams (newVal, oldVal) {
        let currentPageKey = this.apiKeys.currentPage
        if (newVal[currentPageKey] === oldVal[currentPageKey]) {
          this.init()
        }
      }
    },
    computed: {
      apiKeys () {
        return Object.assign({}, {
          currentPage: 'pageNum',
          pageSize: 'pageSize'
        }, this.apiKeysMap)
      },
      listQueryParams () {
        let apiKeys = this.apiKeys || {}
        let listQueryParams = {}
        Object.keys(apiKeys).forEach((key) => {
          let value = apiKeys[key]
          if (typeof value === 'object') {
            if (value.innerKey) {
              this[value.innerKey] = value.value
            }
            listQueryParams[key] = value.value
          } else {
            listQueryParams[value] = this[key]
          }
        })
        return listQueryParams
      }
    },
    methods: {
      init () {
        this.currentPage = 1
        return this.getList({
          [this.apiKeys.currentPage]: 1
        })
      },
      getList (params) {
        // 记录当前请求的id
        this.getListId = this.getListId || 0
        const currGetListId = ++this.getListId
        this.loading = true
        return this.listApi.requestFn(Object.assign({}, this.listQueryParams, params)).then((data) => {
          // 验证是否是最后一个请求
          if (currGetListId === this.getListId) {
            this.listApi.responseFn.call(this, data)
          }
        }).finally(() => {
          if (currGetListId === this.getListId) {
            this.loading = false
          }
        })
      },
      handlePageChange (page) {
        this.currentPage = page
      }
    },
    render (h) {
      const basicColCfg = (prop) => {
        return {
          formatter (row, column) {
            return row[prop] || '--'
          },
          'show-overflow-tooltip': true
        }
      }
      return (
        <div class="search-table">
          {this.$slots['table-tools']}
          <el-table
            {...this.tableAttrs}
            data={this.initData || this.tableData}
            {...{directives: [{ name: 'loading', value: this.loading }]}}>
            {
              this.columnData.map(item => {
                return (
                  item.slotName ? this.$slots[item.slotName] : (
                    <el-table-column
                      align={this.tableAttrs.props ? this.tableAttrs.props.align : 'left'}
                      {...{props: Object.assign({}, basicColCfg(item.attrs.prop), item.attrs)}}
                      scopedSlots={item.scopedSlots}>
                    </el-table-column>
                  )
                )
              })
            }
            { this.$slots.default }
          </el-table>
          {
            this.total > this.pageSize && (
            <div
              class="pagination-wrap">
              <el-pagination
                style="text-align: right; margin-top: 30px"
                current-page={this.currentPage}
                {...{on: { 'current-change': this.handlePageChange }}}
                page-size={this.pageSize}
                layout="prev, pager, next, jumper"
                total={this.total}>
              </el-pagination>
            </div>
            )
          }
        </div>
      )
    }
  }
</script>
<style lang="scss">
  .search-table {
    .table-tools {
      margin-top: 30px;

      .tool-item {
        & + .tool-item {
          margin-left: 20px;
        }
      }

      .el-button {
        width: 80px;
        border-radius: 18px;
      }
    }

    .el-table {
      margin-top: 20px;
    }
  }
</style>
