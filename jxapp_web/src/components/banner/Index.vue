<script>
/*
 * Created by zhengji
 * Date: 2017/8/29
 */
import placeholderImg from '@/assets/images/placeholder.png'

import SearchTable from '@/components/_common/searchTable/SearchTable'

import {
  getListApi,
  deleteBannerBatchApi,
  addBanenrApi,
  modifyBannerApi,
  switchVisibleApi
} from './api'

import { Loading } from 'element-ui'
import EditDialog from './_thumbs/EditDialog.vue'
import ImgZoom from '@/components/_common/imgZoom/ImgZoom.vue'

let adding = false
export default {
  name: 'Banner',
  components: {
    EditDialog,
    ImgZoom,
    SearchTable
  },
  data () {
    this.tableAttrs = {
      'props': {
        'tooltip-effect': 'dark',
        'style': 'width: 100%',
        'align': 'center'
      },
      'on': {
        'selection-change': this.handleSelectionChange.bind(this)
      }
    }
    this.columnData = [{
      attrs: {
        'type': 'selection',
        'width': '90',
        'align': 'left'
      }
    }, {
      attrs: {
        'prop': 'no',
        'label': '排序',
        'min-width': '80'
      }
    }, {
      attrs: {
        'prop': 'name',
        'label': '名称',
        'min-width': '140',
        'show-overflow-tooltip': true
      }
    }, {
      attrs: {
        'prop': 'cover',
        'min-width': '120',
        'label': '封面图'
      },
      scopedSlots: {
        default: (scope) => {
          return (
            <img-zoom
              src={scope.row.cover}
              style="width: 80px;height: 60px;">
            </img-zoom>
          )
        }
      }
    }, {
      attrs: {
        'prop': 'link',
        'label': '跳转链接',
        'min-width': '160',
        'show-overflow-tooltip': true
      },
      scopedSlots: {
        default: (scope) => {
          let link = scope.row.link
          if (!link) {
            return '--'
          } else {
            if (link.indexOf('http') === -1) {
              link = '//' + link
            }
            return (
              <a href={link} target="_blank">{ scope.row.link }</a>
            )
          }
        }
      }
    }, {
      attrs: {
        'min-width': '200',
        'label': '操作'
      },
      scopedSlots: {
        default: (scope) => {
          return (
            <div class="flex--center operations">
              <span
                class="operate-item el-icon-edit"
                onClick={() => this.openEditDialog(scope.row)}>
              </span>
              <span
                class="operate-item el-icon-delete"
                onClick={() => this.delRow(scope.row)}>
              </span>
              <span class="operate-item visible-switch flex--vcenter">
                <el-switch
                  {...{props: { 'on-text': '', 'off-text': '' }}}
                  value={scope.row.visible}
                  onInput={(visible) => (scope.row.visible = visible)}
                  onChange={() => this.switchVisible(scope.row)}>
                </el-switch>
                { scope.row.visible ? '显示' : '隐藏' }
              </span>
            </div>
          )
        }
      }
    }]
    this.listApi = {
      requestFn: getListApi,
      responseFn (data) {
        let content = data.content || {}
        this.tableData = (content.list || []).map((item) => ({
          no: item.orderNumber,
          id: item.id,
          name: item.name,
          cover: item.bannerUrl,
          link: item.jumpUrl,
          visible: !item.display  // display: 0表示显示 1表示隐藏
        }))
        this.total = content.total || 0
      }
    }
    return {
      searchKeyword: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      multipleSelection: [],
      editDialogVisible: false,
      editData: null,
      apiKeysMap: {
        pageSize: {
          value: 10,
          innerKey: 'pageSize' // searchTable组件内部映射的key
        },
        name: {
          value: undefined
        },
        currentPage: 'pageNum'
      }
    }
  },
  created () {
    this.placeholderImg = placeholderImg
  },
  watch: {
    editDialogVisible (val) {
      if (!val) {
        this.editData = null
        adding = false
      }
    }
  },
  methods: {
    // 修改展示数量
    changeDisplayNum () {
      this.$prompt('新展示个数', '修改展示数量', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\s*\d+\s*$/,
        inputErrorMessage: '请填入数字',
        confirmButtonClass: 'dialog-confirm-btn',
        beforeClose (action, instance, done) {
          if (action === 'confirm') {
            const loading = Loading.service({ fullscreen: true })
            setTimeout(() => {
              loading.close()
              console.log(loading)
              done()
            }, 2000)
          } else {
            done()
          }
        }
      }).then(({ value }) => {
        this.$message({
          type: 'success',
          message: '修改成功'
        })
      })
    },
    // 搜索banner
    handleSearch (e) {
      this.apiKeysMap = Object.assign({}, this.apiKeysMap, {
        name: {
          value: this.searchKeyword || undefined
        }
      })
    },
    // 多选
    handleSelectionChange (val) {
      console.log('handleSelectionChange')
      this.multipleSelection = val
    },
    // 编辑或新增
    openEditDialog (rowData, isAdd) {
      this.editDialogVisible = true
      this.editData = rowData
      adding = !!isAdd
    },
    // 删除单个banner
    delRow (row) {
      this.$confirm('是否删除该信息？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            deleteBannerBatchApi({
              bannerIdList: row.id
            }).then(res => {
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
    },
    // 批量删除
    batchRemove () {
      deleteBannerBatchApi({
        bannerIdList: this.multipleSelection.map(item => item.id).join(',')
      }).then(res => {
        this.$message({
          type: 'success',
          message: '删除成功'
        })
        this.editDialogVisible = false
        this.$refs.searchTable.getList()
      })
    },
    handleEditCancel () {
    },
    // 提交编辑或新增
    handleEditSubmit (data, respondCb) {
      let formData
      if (data.file) {
        formData = new FormData()
        formData.append('file', data.file)
      }
      let sendData = {
        name: data.name,
        jumpUrl: data.link,
        orderNumber: data.no,
        bannerId: data.id
      }
      let requestFn = adding ? addBanenrApi : modifyBannerApi
      requestFn(sendData, formData).then(res => {
        this.$message({
          type: 'success',
          message: adding ? '添加成功' : '修改成功'
        })
        this.editDialogVisible = false
        this.$refs.searchTable.init()
        respondCb(true)
      }).catch(() => {
        respondCb()
      })
    },
    // 显示／隐藏
    switchVisible (rowData) {
      switchVisibleApi({
        bannerId: rowData.id
      }).then(res => {
        this.$message({
          type: 'success',
          message: rowData.visible ? '显示成功' : '隐藏成功'
        })
      }).finally(() => {
        this.$refs.searchTable.getList()
      })
    }
  }
}
</script>

<template>
  <div id="banner">
    <div class="flex--vcenter page-top">
      <div class="page-title">
        BANNER管理
      </div>
      <div class="display-num-control">
        <span class="label">展示数量 ：</span>
        <span class="display-num">4个</span>
        <span
          class="el-icon-edit"
          @click="changeDisplayNum">
        </span>
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
            placeholder="请在此输入名称／ID"
            icon="search"
            v-model="searchKeyword"
            @keyup.enter.native="handleSearch"
            :on-icon-click="handleSearch">
          </el-input>
        </div>
        <div class="btn-wrap">
          <el-button
            class="btn--add"
            type="primary"
            @click="openEditDialog(null, true)">
            新增 <i class="el-icon-plus"></i>
          </el-button>
          <el-button
            :disabled="!multipleSelection.length"
            @click="batchRemove">
            批量删除
          </el-button>
        </div>
      </div>
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

  #banner {
    .display-num-control {
      margin-left: 60px;
      .label {
        color: $color3;
      }

      .el-icon-edit {
        color: #adb9ca;
        cursor: pointer;
      }
    }

    .table-tools {
      margin-top: 30px;
      justify-content: space-between;
    }
    .btn-wrap {
      .el-button {
        border-radius: 18px;
      }
    }
    .btn--del {
      background: $bg5;
      color: #fff;
      &:hover {
        border-color: transparent;
      }
    }

    .el-table {
      margin-top: 20px;
      td {
        height: 80px;
      }
    }
    .el-table__header {
      th:first-child {
        .cell {
          &:after {
            content: '全选';
            margin-left: 5px;
          }
        }
      }
    }
    .cover-img {
      vertical-align: middle;
      display: inline-block;
      background: $bg6;
    }
    .cover-noimg {
      background: $bg6 url('~@/assets/images/placeholder.png') center no-repeat;
      background-size: 40px 30px;
    }

    .operate-item {
      .el-switch {
        margin-right: 10px;
      }
    }
    .visible-switch {
      color: $color3;
      font-size: 14px;
    }

    .pagination-wrap {
      margin-top: 30px;
      text-align: right;
    }
  }
</style>
