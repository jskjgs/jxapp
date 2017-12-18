<script>
/*
 * Created by zhengji
 * Date: 2017/9/10
 */
export default {
  name: 'ImgZoom',
  props: {
    src: String
  },
  data () {
    return {
    }
  },
  methods: {
    showZoom () {
      const h = this.$createElement
      this.$msgbox({
        title: '图片预览',
        class: 'ooo',
        customClass: 'img-zoom-box',
        message: h('img', {
          attrs: {
            src: this.src
          }
        })
      })
    }
  }
}
</script>

<template>
  <div class="img-zoom">
    <div class="img-zoom-wrap flex--center">
      <img :src="src" alt="">
      <div class="mask flex--center">
        <i class="el-icon-view" @click="showZoom"></i>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  .img-zoom {
    position: relative;
    display: block;
    margin: auto;

    .img-zoom-wrap {
      width: 100%;
      height: 100%;
    }
    img {
      max-width: 100%;
      max-height: 100%;
    }
    .mask {
      display: none;
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.4);
    }
    &:hover {
      .mask {
        display: flex;
      }
    }
    .el-icon-view {
      color: #fff;
      font-size: 24px;
      cursor: pointer;
      &:hover {
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
</style>
