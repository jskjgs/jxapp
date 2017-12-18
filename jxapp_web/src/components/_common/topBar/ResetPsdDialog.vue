<script>
  import {
    validPsd
  } from '@/utils/validator'

  export default {
    name: 'ResetPsdDialog',
    props: {
      value: Boolean
    },
    data () {
      return {
        formData: {
          oldPsd: '',
          psd: '',
          rePsd: ''
        },
        submitLoading: false
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
    methods: {
      validPsd (rule, val, cb) {
        cb(validPsd(val))
      },
      // 再次输入密码验证
      validRePsd (rule, val, cb) {
        if (val === '') {
          cb(new Error('请再次输入密码'))
        } else if (val !== this.formData.psd) {
          cb(new Error('两次输入不匹配'))
        } else {
          cb()
        }
      },
      // 提交表单
      submit () {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.submitLoading = true
            this.$emit('submit', {
              oldPsd: this.formData.oldPsd,
              psd: this.formData.psd,
              rePsd: this.formData.rePsd
            }, () => {
              this.submitLoading = false
            })
          }
        })
      },
      // 重置表单
      resetForm () {
        this.$refs.ruleForm.resetFields()
      }
    }
  }
</script>

<template>
  <el-dialog
    title="修改密码"
    :visible.sync="visible"
    size="tiny"
    class="dialog--center"
    custom-class="reset-psd-dialog"
    @close="resetForm">
    <el-form
      ref="ruleForm"
      :model="formData"
      label-width="80px">
      <el-form-item
        label="旧密码"
        prop="oldPsd"
        :rules="[{
          required: true,
          message: '请输入旧密码',
          trigger: 'blur'
        }]">
        <el-input
          type="password"
          v-model="formData.oldPsd"
          auto-complete="off"
          placeholder="请输入旧密码">
        </el-input>
      </el-form-item>
      <el-form-item
        label="密码"
        prop="psd"
        required
        :rules="[{
          required: true,
          message: '请输入新密码'
        }]">
        <el-input
          type="password"
          v-model="formData.psd"
          auto-complete="off"
          placeholder="请输入新密码">
        </el-input>
      </el-form-item>
      <el-form-item
        label="确认密码"
        prop="rePsd"
        :rules="[{
          validator: validRePsd, trigger: 'blur'
        }]">
        <el-input
          type="password"
          v-model="formData.rePsd"
          auto-complete="off"
          placeholder="请再次输入新密码">
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button
        type="primary"
        v-loading="submitLoading"
        @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<style lang="scss">
  .reset-psd-dialog {
    .is-required {
      .el-form-item__label:before {
        display: none;
      }
    }
  }
</style>
