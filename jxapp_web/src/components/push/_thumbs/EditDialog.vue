<script>
  /*
   * Created by zhengji
   * Date: 2017/10/21
   */
  import {
    deepAssign
  } from '@/utils/index'
  const initialData = {
    ruleForm: {
      content: '', // 推送内容
      platform: [], // 目标平台
      pushWay: [], // 推送方式
      population: '', // 目标人群
      link: {
        location: '', // 跳转位置
        link: '' // 链接
      },
      pushTime: '', // 发送时间
      retainTime: '1天' // 离线保留时长
    },
    deviceAliasRadio: ''
  }
  export default {
    name: 'EditDialog',
    props: {
      data: {
        type: Object
      },
      value: {
        type: Boolean
      }
    },
    data () {
      this.retainTimeOpts = [{
        text: '不保留'
      }, {
        text: '1分钟'
      }, {
        text: '10分钟'
      }, {
        text: '1小时'
      }, {
        text: '3小时'
      }, {
        text: '12小时'
      }, {
        text: '1天'
      }, {
        text: '3天'
      }, {
        text: '10天'
      }]
      return Object.assign({}, initialData)
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
      },
      visible (val) {
        if (!val) {
          deepAssign(this.$data, initialData)
        }
      }
    }
  }
</script>

<template>
  <el-dialog
    title="编辑推送信息"
    class="edit-dialog push__edit-dialog dialog--center"
    :visible.sync="visible">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      label-width="120px"
      class="demo-ruleForm"
      label-position="left">
      <el-row>
        <el-form-item
          label="推送内容："
          prop="content"
          class="el-form--label-top"
          required
          :rules="[
              { required: true, message: '描述不能为空'},
              { pattern: /^\s*.{0,30}\s*$/, message: '字数不能超30', trigger: 'blur'}
            ]">
          <el-row>
            <el-col :span="16" style="position: relative;">
              <el-input
                v-model="ruleForm.content"
                type="textarea"
                :autosize="false"
                auto-complete="off">
              </el-input>
              <span class="text-length">{{ ruleForm.content ? ruleForm.content.trim().length : 0 }}/30</span>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          label="目标平台："
          prop="platform"
          required>
          <el-checkbox-group
            v-model="ruleForm.platform">
            <el-checkbox label="ios" :key="0">IOS平台</el-checkbox>
            <el-checkbox label="android" :key="1">安卓平台</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
          label="推送方式："
          prop="pushWay"
          required>
          <el-checkbox-group
            v-model="ruleForm.pushWay">
            <el-checkbox label="0" :key="0">应用内推送</el-checkbox>
            <el-checkbox label="1" :key="1">系统推送</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
          label="目标人群："
          prop="population"
          required>
          <el-radio-group
            v-model="ruleForm.population"
            size="small">
            <el-radio-button label="0" :key="0">广播</el-radio-button>
            <el-radio-button label="1" :key="1">设备别名</el-radio-button>
          </el-radio-group>
          <el-form-item
            prop="inputType">
            <el-row>
              <el-radio class="radio" v-model="deviceAliasRadio" label="1">手动输入</el-radio>
              <el-radio class="radio" v-model="deviceAliasRadio" label="2">文件上传</el-radio>
            </el-row>
            <el-row>
              <el-col v-if="deviceAliasRadio === '1'" :span="16" style="position: relative;">
                <el-input
                  v-model="ruleForm.describe"
                  :autosize="false"
                  auto-complete="off"></el-input>
                <span class="text-length">{{ ruleForm.name ? ruleForm.name.length : 0 }}/30</span>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form-item>
        <el-form-item
          label="跳转链接："
          prop="link.location"
          class="el-form--label-top"
          required>
          <el-row :gutter="10">
            <el-col :span="8">
              <el-select v-model="ruleForm.link.location" placeholder="请选择">
                <el-option
                  v-for="item in [{label: 'BANNER'}, {label: '孕产日记'}]"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="14" style="position: relative;">
              <el-input v-model="ruleForm.link.link" placeholder="请输入内容"></el-input>
              <span class="text-length">8/10</span>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          label="发送时间："
          prop="pushTime"
          required>
          <el-radio-group
            class="inline-block"
            v-model="ruleForm.pushTime"
            size="small">
            <el-radio-button label="0" :key="0">立即发送</el-radio-button>
            <el-radio-button label="1" :key="1">定时发送</el-radio-button>
          </el-radio-group>
          <el-date-picker
            v-if="ruleForm.pushTime == 1"
            style="margin-left: 10px;"
            v-model="ruleForm.pushTime"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="离线保留时长："
          prop="retainTime">
          <el-row>
            <el-col :span="6">
              <el-select v-model="ruleForm.retainTime" placeholder="请选择">
                <el-option
                  v-for="item in retainTimeOpts"
                  :key="item.text"
                  :label="item.text"
                  :value="item.text">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<style lang="scss">
  .push__edit-dialog {
    .el-radio-button {
      & + .el-radio-button {
        margin-left: 20px;
      }
      .el-radio-button__inner {
        border-radius: 4px;
        border-color: transparent;
      }
    }
    .el-radio-button__inner {
      border-color: transparent;
      width: 80px;
      background: #e7e7e7;
    }

    .el-radio-button.is-checked .el-radio-button__inner {
      box-shadow: none;
    }
  }
</style>
