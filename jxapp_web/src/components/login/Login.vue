<script>
  import loginApi from './api'
  import { mapMutations } from 'vuex'
  import {
    UPDATE_ACCOUNTINFO
  } from '@/store/global'
  export default {
    name: 'Login',
    data () {
      return {
        // 表单字段
        loginForm: {
          userName: '',
          psd: ''
        },
        loginLoading: false
      }
    },
    methods: {
      // 清除输入框
      clearInput (formKey) {
        this.loginForm[formKey] = ''
      },
      // 表单提交
      submitForm () {
        // 验证
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.loginLoading = true
            loginApi({
              account: this.loginForm.userName,
              password: this.loginForm.psd
            }).then((res) => {
              const accountInfo = res.content
              const accountInfoJson = encodeURIComponent(JSON.stringify(accountInfo))
              localStorage.setItem('accountInfo', accountInfoJson)
              this.updateAccountInfo(accountInfo)
              this.$router.push('/')
            }).finally(() => {
              this.loginLoading = false
            })
          }
        })
      },
      ...mapMutations({
        updateAccountInfo: UPDATE_ACCOUNTINFO // 更新vuex中的accountInfo和auth
      })
    }
  }
</script>

<template>
  <div id="login-wrap" class="flex--center">
    <div class="login-panel flex">
      <div class="left-banner flex-item--none">
      </div>
      <div class="login-form flex-item">
        <h3 class="login-form--title">欢迎登录后台管理系统</h3>
        <el-form
          ref="loginForm"
          :model="loginForm">
          <el-form-item
            prop="userName"
            :rules="[
              { required: true, message: '请输入用户名', trigger: 'blur' }
            ]">
            <el-input
              autofocus
              placeholder="请输入用户名"
              v-model.trim="loginForm.userName"
              :disabled="loginLoading"
              :icon="loginForm.userName.trim().length > 0 ? 'circle-close' : ''"
              :on-icon-click="(event) => {
                clearInput('userName')
              }"></el-input>
          </el-form-item>
          <el-form-item
            prop="psd"
            :rules="[
              { required: true, message: '请输入密码', trigger: 'blur' }
            ]">
            <el-input
              placeholder="请输入密码"
              type="password"
              v-model.trim="loginForm.psd"
              @keyup.enter.native="submitForm"
              :disabled="loginLoading"
              :icon="loginForm.psd.trim().length > 0 ? 'circle-close' : ''"
              :on-icon-click="(event) => {
                clearInput('psd')
              }"></el-input>
          </el-form-item>
          <el-button
            class="submit-btn"
            type="primary"
            :disabled="loginLoading"
            v-loading="loginLoading"
            @click="submitForm">
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  #login-wrap {
    width: 100%;
    height: 100%;

    .login-panel {
      overflow: hidden;
      border-radius: 5px;
      width: 800px;
      height: 550px;

      box-shadow: 0 8px 16px 0 rgba(7,17,27,.2);
    }

    .left-banner {
      width: 440px;
      height: 100%;

      text-align: center;
      background: url('./images/login-banner.png') no-repeat;
      background-size: contain;
      color: #fff;

      .logo {
        margin-top: 200px;
      }
      .title {
        margin-top: 30px;

        font-size: 24px;
        font-weight: 400;
      }

      .copyright {
        margin-top: 150px;

        font-size: 12px;
      }
    }

    .login-form {
      height: 100%;
      padding: 24px;

      background: #fff;

      @at-root {
        &--title {
          margin-top: 120px;

          font-size: 24px;
        }
      }
    }

    .submit-btn {
      width: 100%;

      border-color: #424c64;
      background: #424c64;
    }
  }
</style>

