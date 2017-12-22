<script>
/*
 * Created by zhengji
 * Date: 2017/9/5
 */
export default {
  name: 'ImgUploader',
  props: {
    imgSrc: {
      type: String
    }
  },
  watch: {
    imgSrc: {
      handler (newSrc) {
        this.previewSrc = newSrc
      },
      immediate: true
    }
  },
  data () {
    return {
      missFile: false,
      previewSrc: '',
      fileInputValid: true
    }
  },
  methods: {
    clearFileInput () {
      this.fileInputValid = false
      this.previewSrc = ''
      this.missFile = false
      this.$emit('file-change', null)
      this.$nextTick(() => {
        this.fileInputValid = true
      })
    },
    handleFileChange (e) {
      let file = e.target.files[0]
      this.validFile(file).then((blobURl) => {
        this.$emit('file-change', file)
        this.previewSrc = blobURl
      }).catch((errorMsg) => {
        this.$emit('file-change', null)
        if (errorMsg) {
          this.$message({
            type: 'warning',
            message: errorMsg.join('/n')
          })
        }
      })
    },
    checkFileExist (file) {
      return new Promise((resolve, reject) => {
        if (!file && !this.previewSrc) {
          this.missFile = true
          reject()
        } else {
          resolve()
        }
      })
    },
    validFile (file) {
      let vm = this
      let windowUrl = window.URL || window.webkitURL
      let blobURl = windowUrl.createObjectURL(file)
      let isImg = true
      let valid = true
      let message = []
      if (file.size / 1024 > 1024) {
        valid = false
        message.push('尺寸不应大于1M')
      }
      if (!(file.type === 'image/jpeg' || file.type === 'image/jpg' || file.type === 'image/png')) {
        isImg = false
        valid = false
        message.push('文件类型只能是jpg或png')
      }
      return new Promise((resolve, reject) => {
        if (isImg) {
          let img = new Image()
          img.src = blobURl
          img.onload = function () {
            // if (this.width < 1280 || this.height < 740) {
            //   valid = false
            //   message.push('图片尺寸不小于1280*740')
            // }
            if (valid) {
              vm.missFile = false
              resolve(blobURl)
            } else {
              reject(message)
            }
          }
        } else {
          reject(message)
        }
      })
    }
  }
}
</script>

<template>
  <div class="img-uploader">
    <div class="flex">
      <div
        class="upload-box flex-item--none"
        :class="missFile ? 'miss-file' : ''"
        :style="{ backgroundImage: `url(${previewSrc})` }">
        <i v-show="!previewSrc" class="el-icon-plus"></i>
        <input
          v-if="fileInputValid"
          type="file"
          class="upload-input"
          @change="handleFileChange">
        <i
          v-show="previewSrc"
          class="el-icon-close"
          @click="clearFileInput">
        </i>
      </div>
      <ul class="upload-notices" style="margin-left: 40px">
        <li class="upload-notice">1、图片大小不能超过1MB</li>
        <!-- <li class="upload-notice">2、图片必须为长方形，且图片尺寸必须大于或等于1280*740</li> -->
        <li class="upload-notice">3、仅支持jpg／png两种格式</li>
        <li class="upload-notice">3、仅支持jpg／png两种格式</li>
      </ul>
    </div>
    <div class="el-form-item__error" v-if="missFile">请上传文件</div>
  </div>
</template>

<style lang="scss">
  @import "~@/assets/style/variables/index";
  .img-uploader {
    .upload-box {
      position: relative;
      border: 1px dashed #c0ccda;
      width: 118px;
      height: 118px;
      overflow: hidden;
      background-size: contain;
      background-position: center;
      background-repeat: no-repeat;

      &.miss-file {
        border-color: #ff4949;
      }

      .el-icon-plus {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        font-size: 18px;
        color: $color1;
      }

      .el-icon-close {
        position: absolute;
        top: 5px;
        right: 5px;
        color: #b2b2b2;
        cursor: pointer;
      }
    }
    .upload-input {
      position: absolute;
      opacity: 0;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      cursor: pointer;
    }
    .upload-notice {
      line-height: 1.4;
      color: $color5;
      & + .upload-notice {
        margin-top: 5px;
      }
    }
  }
</style>
