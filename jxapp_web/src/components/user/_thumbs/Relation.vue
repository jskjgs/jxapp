<script>
  /*
   * Created by zhengji
   * Date: 2017/10/21
   */
  import SearchTable from '@/components/_common/searchTable/SearchTable'
  import {
    relationApi
  } from '../api'

  export default {
    name: 'Relation',
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
          'prop': 'name',
          'label': '姓名',
          'min-width': '140'
        }
      }, {
        attrs: {
          'prop': 'phone',
          'label': '手机号',
          'min-width': '140'
        }
      }, {
        attrs: {
          'prop': 'mzh',
          'label': '就诊号',
          'min-width': '140'
        }
      }, {
        attrs: {
          'prop': 'idCard',
          'label': '身份证号',
          'min-width': '180'
        }
      }]
      this.listApi = {
        requestFn: relationApi,
        responseFn (data) {
          let content = data.content || {}
          this.tableData = content.map((item) => ({
            name: item.name,
            phone: item.phone,
            mzh: item.mzh,
            idCard: item.idCard
          }))
          this.total = content.total || 0
        }
      }
      return {
        apiKeysMap: {
          accountId: {
            value: this.$route.params.accountId
          }
        }
      }
    }
  }
</script>

<template>
  <div>
    <search-table
      ref="searchTable"
      :table-attrs="tableAttrs"
      :column-data="columnData"
      :list-api="listApi"
      :api-keys-map="apiKeysMap">
    </search-table>
  </div>
</template>

<style lang="scss" scoped>
</style>
