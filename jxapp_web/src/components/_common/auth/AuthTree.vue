<script>
  import RenderContent from './RenderContent'

  export default {
    name: 'AuthTree',
    props: {
      type: {
        type: String,
        default: 'use' // configure / use
      },
      treeData: Array
    },
    data () {
      return {
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        topNodeAdding: false, // 添加一级分类中
        topNode: { // 添加一级分类的结点表单数据
          id: '',
          label: ''
        },
        topNodeUploading: false,
        nodeIdVisible: false
      }
    },
    watch: {
      topNodeAdding (val) {
        if (!val) {
          this.topNode = {
            id: '',
            label: ''
          }
        }
      }
    },
    methods: {
      switchNodeId () {
        this.nodeIdVisible = !this.nodeIdVisible
      },
      // 获取勾选的结点（平级数组）
      getCheckedKeys () {
        return this.$refs.authTree.getCheckedKeys()
      },
      // 获取勾选的结点
//      getCheckedNodes (leafOnly) {
//        return this.$refs.authTree.getCheckedNodes(leafOnly)
//      },
      // 设置勾选的结点（通过node-key）
      setCheckedKeys (keys) {
        this.$refs.authTree.setCheckedKeys(keys)
      },
      // 添加一级结点
      addTopNode () {
        this.topNodeAdding = true
      },
      // 提交添加的一级结点
      submitTopNode () {
        this.$refs.topNodeForm.validate((valid) => {
          if (valid) {
            // 深拷贝treeData
            let _treeData = JSON.parse(JSON.stringify(this.treeData))
            let topNodeId = this.topNode.id
            let hasSameId = false
            // 遍历树结点, 检测是否有相同的id
            _treeData.some(function map (item) {
              let children = item.children || []
              if (item.id === topNodeId) {
                hasSameId = true
                return true
              } else {
                children.some((child) => {
                  return map(child)
                })
              }
            })
            if (hasSameId) {
              this.$message({
                type: 'warning',
                message: '有相同id'
              })
              return
            }
            _treeData.push(this.topNode)
            // 替换整个树
            this.$emit('update:treeData', _treeData)
            this.$nextTick(() => {
              this.topNodeAdding = false
            })
          }
        })
      },
      // 取消一级结点提交
      cancelTopNode () {
        this.$refs.topNodeForm.resetFields()
        this.topNodeAdding = false
      },
      // 添加结点
      append (store, data, newNode) {
        // 当前结点id
        let currentId = data.id
        // 深拷贝treeData
        let _treeData = JSON.parse(JSON.stringify(this.treeData))
        // 遍历树结点
        _treeData.forEach(function map (item) {
          let children = item.children || []
          if (item.id === currentId) {
            if (children.length === 0) {
              children = item.children = []
            }
            children.push(newNode)
          } else {
            item.children = children.map((child) => {
              return map(child)
            })
          }
          return item
        })
        // 替换整个树
        this.$emit('update:treeData', _treeData)
      },
      // 移除结点
      remove (store, data) {
        let currentId = data.id
        let _treeData = JSON.parse(JSON.stringify(this.treeData))
        // 判断是否为一级结点
        let firstLevelIndex = _treeData.findIndex(function (item) {
          return item.id === currentId
        })
        if (firstLevelIndex !== -1) {
          _treeData.splice(firstLevelIndex, 1)
        } else {
          _treeData.forEach(function map (item) {
            let children = item.children || []
            let matchIndex = -1
            let childMatched = children.some((child, childIndex) => {
              if (child.id === currentId) {
                matchIndex = childIndex
                return true
              }
            })
            if (childMatched) {
              if (children.length <= 1) {
                item.children = null
              } else {
                item.children.splice(matchIndex, 1)
              }
            } else if (children.length > 0) {
              item.children = children.map((child) => {
                return map(child)
              })
            }
            return item
          })
        }
        this.$emit('update:treeData', _treeData)
      },
      // 编辑结点
      update (store, data, updatedNode) {
        let currentId = data.id
        let _treeData = JSON.parse(JSON.stringify(this.treeData))
        console.log(JSON.parse(JSON.stringify(this.treeData)))
        _treeData.forEach(function map (item) {
          let children = item.children = item.children || []

          if (item.id === currentId) {
            item.id = updatedNode.id
            item.label = updatedNode.label
          }
          item.children = children.map((child) => {
            return map(child)
          })
          return item
        })
        this.$emit('update:treeData', _treeData)
      },
      // 渲染结点显示内容
      renderContent (h, args) {
        let {node, data, store} = args
        let content = {
          template:
            `<span>
              <span>{{ node.label }}</span>
            </span>`,
          data () {
            return {
              node: node,
              data: data
            }
          },
          watch: {
            'node.checked' (val) {
              if (val) {
                node.expand()
              } else {
                node.collapse()
                // 循环遍历处理
                let recursive = (node, cb) => {
                  node.childNodes.forEach((child) => {
                    cb(child)
                    recursive(child, cb)
                  })
                }
                recursive(node, (child) => {
                  // 勾选框置空
                  child.setChecked(this.data, false)
                  // 折叠
                  child.collapse()
                })
              }
              console.log('checked', val)
            }
          }
        }
        let renderComponent = this.type === 'configure' ? RenderContent : content
        return h(renderComponent, {
          props: {
            node,
            data,
            store,
            update: this.update,
            append: this.append,
            remove: this.remove,
            idVisible: this.nodeIdVisible
          }
        })
      }
    }
  }
</script>

<template>
  <div class="auth-tree-wrap" :class="'auth-tree-' + type">
    <div class="flex" style="justify-content: space-between">
      <el-button
        v-if="type === 'configure' ? true : false"
        @click="switchNodeId">
        {{ nodeIdVisible ? '隐藏' : '显示' }}节点id
      </el-button>
      <transition
        v-if="type === 'configure' ? true : false"
        :name="topNodeAdding ? 'slide-right' : 'slide-left'"
        mode="out-in">
        <el-form
          v-if="topNodeAdding"
          ref="topNodeForm"
          class="inline-block"
          :inline="true"
          :model="topNode">
          <el-form-item
            prop="id"
            :rules="[{
              required: true, message: '字段不能为空', trigger: 'blur'
            }]">
            <el-input
              placeholder="id"
              size="small"
              :disabled="topNodeUploading"
              v-model.trim="topNode.id">
            </el-input>
          </el-form-item>
          <el-form-item
            prop="label"
            :rules="[{
              required: true, message: '字段不能为空', trigger: 'blur'
            }]">
            <el-input
              placeholder="权限名"
              size="small"
              :disabled="topNodeUploading"
              v-model.trim="topNode.label">
            </el-input>
          </el-form-item>
          <el-button
            type="primary"
            style="margin-left: 10px"
            v-loading="topNodeUploading"
            :disabled="topNodeUploading"
            @click="submitTopNode">
            确定
          </el-button>
          <el-button
            @click="cancelTopNode"
            :disabled="topNodeUploading">
            取消
          </el-button>
        </el-form>
        <el-button
          type="primary"
          v-else
          @click="addTopNode"
          style="margin-left: 20px">
          添加一级节点
        </el-button>
      </transition>
    </div>
    <el-tree
      ref="authTree"
      class="auth-tree"
      style="margin-top: 20px;"
      :default-expand-all="type === 'configure' ? true : false"
      :auto-expand-parent="true"
      :data="treeData"
      :props="defaultProps"
      :show-checkbox="type === 'configure' ? false : true"
      check-strictly
      node-key="id"
      :expand-on-click-node="false"
      :render-content="renderContent">
    </el-tree>
  </div>
</template>

<style lang="scss">
  @import '../../../assets/style/core/_layout';
  .auth-tree-wrap {
    border-radius: 2px;
    max-width: 700px;
    .el-tree-node__content {
      @extend .flex--vcenter;
    }
    .el-tree-node__expand-icon {
      @extend .flex-item--none;
    }

    .el-form-item {
      margin: 0;
      width: 100px;
    }
  }

  .auth-tree-use {
    .el-tree-node__expand-icon {
      display: none;
    }

    .el-checkbox {
      margin-left: 20px;
    }
  }
</style>
