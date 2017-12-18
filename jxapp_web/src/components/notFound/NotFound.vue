<script>
export default {
  name: 'NotFound',
  data () {
    return {
    }
  },
  methods: {
    backToIndex () {
      this.$router.push('/')
    }
  }
}
</script>

<template>
  <div id="not-found">
    <div class="not-found-image">
      <img src="./images/notFound.png" alt="404"/>
    </div>
    <div class="btn-wrap">
      <p class="tip">糟糕，你访问的网页飞到外太空了</p>
      <el-button
        type="primary"
        class="back-to-index"
        @click="backToIndex">返回首页</el-button>
    </div>
  </div>
</template>

<style lang="scss">
  #not-found {
    position: relative;
    padding-top: 10vh;
    width: 100%;
    height: 100%;

    .not-found-image {
      width: 100%;
      height: 70vh;

      img {
        display: block;
        max-height: 100%;
        max-width: 100%;
        margin: auto;
      }
    }

    .btn-wrap {
      position: absolute;
      bottom: 50px;
      left: 0;
      right: 0;
      margin: auto;
      text-align: center;
    }
    .tip {
      font-size: 24px;
      color: #20a0ff;
    }
    .back-to-index {
      border-radius: 2px;
    }
  }
</style>
