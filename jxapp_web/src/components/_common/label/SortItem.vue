<script>
  export default {
    name: 'SortItem',
    props: {
      chosen: null,
      type: String, // configure / use
      sort: Object, // 当前分类数据
      index: Number // 当前项在数组中的下标
    },
    created () {
      if (this.sort.new) {
        this.editing = true
      }
    },
    data () {
      return {
        editing: false, // 是否处于编辑状态
        // 当前分类数据的拷贝（用于表单编辑）
        edits: '',
        submitLoading: false // 保存ing
      }
    },
    computed: {
      // 当前分类的勾选项
      chosenProxy: {
        get () {
          let isRadio = this.sort.attribute === 1
          let currchosen = this.chosen[this.sort.title.id] // 字符串
          if (isRadio) {
            currchosen = currchosen ? [currchosen] : []
          }
          return currchosen
        },
        set (val) {
          if (this.sort.attribute === 1) {
            if (val.length === 0) {
              val = ''
            } else {
              let index = val.indexOf(this.chosenProxy)
              val = val.concat().splice(index, 1)
              val = val[0]
            }
          }
          this.$emit('update:chosen', Object.assign({}, this.chosen, {
            [this.sort.title.id]: val
          }))
        }
      }
    },
    methods: {
      handleChooseClick () {
        // this.$nextTick(() => {
        //   if (this.sort.attribute === 1 && this.sort.labelInfoList.length === 1 && this.chosenProxy) { // 单选
        //     this.chosenProxy = ''
        //   }
        // })
      },
      // 编辑做的操作
      toEdit () {
        this.editing = true
      },
      // 取消编辑
      cancelEdit () {
        if (this.sort.new) {
          this.$emit('delete', {
            index: this.index,
            sort: this.edits
          })
        }
        this.editing = false
        this.$refs.ruleForm.resetFields()
      },
      // 保存修改过后的数据
      submitForm () {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.submitLoading = true
            // 判断new字段的sort,如果是true就执行以下代码
            let _edits = JSON.parse(JSON.stringify(this.edits))
            if (!this.sort.new) {
              let addLabelList = _edits.addLabelList = []
              _edits.labelInfoList = _edits.labelInfoList.filter(item => {
                if (!item.id) {
                  addLabelList.push(item)
                  return false
                } else {
                  return true
                }
              })
            }

            this.$emit('update', {
              index: this.index,
              sort: _edits
            }, () => {
              this.submitLoading = false
              this.edits = {}
              this.editing = false
            })
          }
        })
      },
      // 添加标签
      addLabel () {
        // labelInfoList 后台字段，拷贝在edits里面
        let labels = this.edits.labelInfoList
        labels.push({
          name: ''
        })
      },
      // 删除添加的标签
      delLabel (index) {
        let removeLabelList = this.edits.removeLabelList
        if (!(removeLabelList && removeLabelList.length)) {
          removeLabelList = this.edits.removeLabelList = []
        }
        removeLabelList.push(this.edits.labelInfoList.splice(index, 1)[0])
      },
      // 删除整个标签
      delAllLabel () {
        this.$emit('delete', {
          sort: this.edits
        }, () => {
          this.editing = false
        })
      },
      // 验证分类名称
      validTitle (rule, val, cb) {
        if (val === '') {
          cb(new Error('请输入分类名称！'))
        } else if (/^[\u4e00-\u9fa5]{2,20}$/g.test(val)) {
          cb()
        } else {
          cb(new Error('分类名称格式不对!'))
        }
      },
      // 验证添加标签
      validLabel (rule, val, cb) {
        if (val === '') {
          cb(new Error('请输入标签内容'))
        } else if (/^[\u4e00-\u9fa5]{2,20}$/g.test(val)) {
          cb()
        } else {
          cb(new Error('标签内容格式不对！'))
        }
      }
    },
    watch: {
      // 监听状态变化（展示／编辑）
      editing (val) {
        if (val) {
          this.edits = JSON.parse(JSON.stringify(this.sort))
        }
      },
      // 监听勾选框类型变化
      'sort.attribute' (val) {
        this.chosenProxy = val === 1 ? '' : []
      }
    }
  }
</script>

<template>
  <div class="sort-item">
    <div
      v-if="!editing"
      key="edit"
      class="sort-item-display"
      :key="sort.title.id">
      <div class="flex" style="justify-content: space-between">
        <h4 class="sort-item-title">{{ sort.title && sort.title.name }}</h4>
        <el-button
          v-if="type === 'configure'"
          class="flex-item--none"
          type="primary"
          @click="toEdit" >
          编辑
        </el-button>
      </div>
      <div class="sort-item-content">
        <el-checkbox
          style="width: 150px;"
          :class="{ 'radio-appearance': sort.attribute === 1 }"
          :disabled="type === 'configure'"
          v-model="chosenProxy"
          v-for="label in sort.labelInfoList"
          :key="label.id"
          :label="label.id + ''"
          @click.native="handleChooseClick">
          {{ label.name }}
        </el-checkbox>
      </div>
    </div>

    <div
      v-else-if="edits"
      class="sort-item-content"
      key="true">
      <el-form
        :model="edits"
        ref="ruleForm">
        <div class="flex" style="justify-content: space-between;align-items: flex-start">
          <div>
            <el-form-item
              class="inline-block"
              prop="title.name"
              :rules="[{
                validator: validTitle, trigger: 'blur'
              }]">
              <el-input
                style="width: 100px; margin: 0;"
                v-model.trim="edits.title.name">
              </el-input>
            </el-form-item>
            <el-radio-group
              v-model="edits.attribute"
              style="margin-left: 10px;">
              <el-radio :label="1">单选</el-radio>
              <el-radio :label="2">多选</el-radio>
            </el-radio-group>
          </div>
          <el-button
            class="flex-item--none"
            type="primary"
            @click="addLabel">
            添加标签
          </el-button>
        </div>
        <div style="margin-top: 20px">
          <el-form-item
            class="inline-block"
            style="margin-right: 10px"
            v-for="(label, index) in edits.labelInfoList"
            :key="label.id"
            :prop="'labelInfoList.' + index + '.name'"
            :rules="[{
              validator: validLabel, trigger: 'blur'
            }]">
            <el-input
              v-model.trim="label.name"
              placeholder="编辑标签"
              :on-icon-click="function(){delLabel(index)}"
              icon="circle-close"
              style="width: 120px;">
            </el-input>
          </el-form-item>
        </div>
        <div class="operate-btn">
          <el-button
            v-if="!sort.new"
            type="primary"
            @click="delAllLabel">
            删除
          </el-button>
          <el-button
            type="primary"
            @click="cancelEdit">
            取消
          </el-button>
          <el-button
            type="primary"
            v-loading="submitLoading"
            :disabled="submitLoading"
            @click="submitForm">
            保存
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss">
  $item-mr: 50px;
  .sort-item {
    overflow: hidden;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 10px;

    @at-root  {
      &-title {
        margin: 0;
      }
      &-display {
        .sort-item-content {
          width: calc(100% + #{$item-mr});
        }
      }
      &-content {
        margin-top: 15px;
      }
    }

    .operate-btn {
      text-align: center;
    }

    .el-checkbox {
      &.radio-appearance {
        .el-checkbox__inner {
          border-radius: 50%;
        }
      }
    }

    .el-checkbox__input.is-disabled+.el-checkbox__label,
    .el-radio__input.is-disabled+.el-radio__label {
      color: #1F2D3E;
    }

    .el-radio,
    .el-radio+.el-radio,
    .el-checkbox,
    .el-checkbox+.el-checkbox {
      margin: 10px #{$item-mr} 0 0 ;
    }
  }

</style>
