<script>
/*
 * Created by zhengji
 * Date: 2017/8/30
 */
let fileObj = ''

import ImgUploader from '@/components/_common/imgUploader/ImgUploader.vue'

const initData = {
  submitLoading: false,
  fileInputValid: true
}

export default {
  name: 'EditDialog',
  components: {
    ImgUploader
  },
  props: {
    value: {
      type: null
    },
    data: {
      type: Object
    }
  },
  data () {
    return {
      form: {
        name: '',
        describe: ''
      },
      submitLoading: false,
      fileInputValid: true
    }
  },
  computed: {
    visible: {
      get () {
        return this.value
      },
      set (val) {
        this.$emit('input', val)
      }
    }
  },
  watch: {
    data (val) {
      if (val) {
        Object.assign(this.form, this.data)
      }
    }
  },
  methods: {
    handleCancel () {
      this.visible = false
      this.$emit('cancel')
    },
    handleSubmit () {
      this.$refs.ruleForm.validate((valid) => {
        let checkFileExist = this.$refs.imgUploader.checkFileExist(fileObj)
        if (valid) {
          checkFileExist.then(() => {
            this.submitLoading = true
            this.$emit('submit', Object.assign({}, this.form, {
              file: fileObj
            }), (success) => {
              this.submitLoading = false
              if (success) {
                this.visible = false
              }
            })
          })
        }
      })
    },
    handleClose () { // 清空数据
      fileObj = ''
      Object.keys(initData).forEach(key => {
        this[key] = initData[key]
      })
      this.$refs.ruleForm.resetFields()
      this.form = {
        name: '',
        describe: ''
      }
      this.$refs.imgUploader.clearFileInput()
    },
    handleFileChange (newFile) {
      fileObj = newFile
    }
  }
}
</script>

<template>
  <div class="edit-dialog doctor__edit-dialog">
    <el-dialog
      class="dialog--center"
      title="编辑信息"
      :visible.sync="visible"
      @close="handleClose">
      <el-form
        ref="ruleForm"
        :model="form"
        label-position="top">
        <el-row>
          <el-col :span="16">
            <el-form-item class="doctor-name" label="姓名：">
              {{ form.name }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="16">
            <el-form-item
              label="描述信息"
              prop="describe"
              required
              :rules="[
                { required: true, message: '描述不能为空'},
                { pattern: /^\s*.{0,30}\s*$/, message: '字数不能超30', trigger: 'blur'}
              ]">
              <el-input
                v-model="form.describe"
                type="textarea"
                :autosize="false"
                auto-complete="off">
              </el-input>
              <span class="text-length">{{ form.describe ? form.describe.trim().length : 0 }}/30</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="照片"
          required>
          <img-uploader
            ref="imgUploader"
            :img-src="form.avatar"
            @file-change="handleFileChange"></img-uploader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="handleCancel"
          :disabled="submitLoading">
          取 消
        </el-button>
        <el-button
          type="primary"
          :disabled="submitLoading"
          v-loading="submitLoading"
          @click="handleSubmit">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss">
  @import "~@/assets/style/variables/index";
  .doctor__edit-dialog {
    .doctor-name {
      .el-form-item__label {
        text-align: left;
        float: left;
        padding: 11px 12px 11px 0;
      }
      .el-form-item__content {
        color: $color5;
      }
    }

    .banner-name {
      .el-input__inner {
        padding-right: 50px;
      }
    }
  }
</style>
