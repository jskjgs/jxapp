<script>
  export default {
    name: 'RenderContent',
    props: {
      node: Object,
      data: Object,
      store: Object,
      update: Function,
      append: Function,
      remove: Function,
      idVisible: Boolean
    },
    created () {
    },
    data () {
      return {
        authNode: {
          id: '',
          label: ''
        },
        nodeStatus: '' // editing / appending
      }
    },
    watch: {
      nodeStatus (val) {
        if (val === '') {
          this.authNode = {
            id: '',
            label: ''
          }
        }
      }
    },
    methods: {
      // mouseout处理函数
      handleMouseleave () {
//        this.nodeStatus = ''
      },
      // 编辑/提交结点名
      edit () {
        if (this.nodeStatus === 'editing') {
          this.$refs.ruleForm.validate((valid) => {
            if (valid) {
              this.update(this.store, this.data, {
                ...this.authNode
              })
              this.nodeStatus = ''
            }
          })
        } else {
          this.nodeStatus = 'editing'
          this.authNode = {
            label: this.data.label,
            id: this.data.id + ''
          }
        }
      },
      // 添加结点／提交添加的结点
      add () {
        if (this.nodeStatus === 'appending') {
          // push数据
          this.$refs.ruleForm.validate((valid) => {
            if (valid) {
              this.append(this.store, this.data, {
                ...this.authNode
              })
              this.nodeStatus = ''
            }
          })
        } else {
          this.nodeStatus = 'appending'
        }
      },
      // 删除结点
      del () {
        this.remove(this.store, this.data)
      },
      // 取消添加或编辑
      cancel () {
        this.nodeStatus = ''
      }
    }
  }
</script>

<template>
  <span
    class="tree-render-content"
    @mouseleave="handleMouseleave">
    <span>
      <span>{{ data.label }}</span>
      <span class="node-id" v-show="idVisible">{{ data.id }}</span>
    </span>
    <span class="tree-node-operate">
      <transition :name="nodeStatus === '' ? 'slide-right' : 'slide-left'">
        <el-form
          v-if="nodeStatus"
          ref="ruleForm"
          :show-message="true"
          :inline="true"
          :model="authNode"
          class="inline-block">
          <el-form-item
            prop="id"
            :rules="[{
              required: true, message: '字段不能为空', trigger: 'blur'
            }]">
            <el-input
              placeholder="id"
              size="small"
              v-model.trim="authNode.id"></el-input>
          </el-form-item>
          <el-form-item
            prop="label"
            :rules="[{
              required: true, message: '字段不能为空', trigger: 'blur'
            }]">
            <el-input
              placeholder="权限名"
              size="small"
              v-model.trim="authNode.label"></el-input>
          </el-form-item>
        </el-form>
      </transition>
      <el-button
        v-if="nodeStatus === 'editing' || nodeStatus === ''"
        type="primary"
        size="mini"
        @click="edit">{{ nodeStatus === 'editing' ? '确认' : '编辑' }}</el-button>
      <el-button
        v-if="nodeStatus === 'appending' || nodeStatus === ''"
        :type="nodeStatus === 'appending' ? 'primary' : 'default' "
        size="mini"
        @click="add">添加</el-button>
      <el-button
        size="mini"
        :type="nodeStatus === '' ? 'danger' : 'default' "
        @click="() => {
          if (this.nodeStatus !== '') {
            this.cancel()
          } else {
            this.del()
          }
        }">{{ nodeStatus === '' ? '删除' : '取消' }}</el-button>
    </span>
  </span>
</template>

<style lang="scss">
  @import '../../../assets/style/core/_layout';

  .tree-render-content {
    @extend .flex-item;

    .node-id {
      margin-left: 5px;
      padding: 0 5px;
      color: #ff4949;
      border: 1px dotted #20a0ff;
      font-size: 0.8em;
    }

    .tree-node-operate {
      display: none;
      float: right;
      margin-right: 20px;

      .el-tree-node__content:hover & {
        display: block;
      }
    }

    .el-form-item {
      margin: 0;
      width: 100px;
    }
  }
</style>
