<script>
  /*
   * Created by zhengji
   * Date: 2017/10/18
   */
  import E from 'wangeditor'

  export default {
    name: 'editor',
    data () {
      return {
        editorContent: ''
      }
    },
    methods: {
      getContent: function () {
        alert(this.editorContent)
      }
    },
    mounted () {
      var editor = new E('#editorElem')
      // 菜单配置
      editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'italic',  // 斜体
        'underline',  // 下划线
        'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'image',  // 插入图片
        'table'  // 表格
      ]
      editor.customConfig.onchange = (html) => {
        this.editorContent = html
      }
      /* 图片上传配置 */
      // 配置服务器端地址
      editor.customConfig.uploadImgServer = '/upload'
      // 将 timeout 时间改为 3s
      editor.customConfig.uploadImgTimeout = 3000
      // 自定义提示方法
      editor.customConfig.customAlert = (info) => {
        this.$message({
          message: '上传失败',
          type: 'error'
        })
      }
      // 监听函数
      editor.customConfig.uploadImgHooks = {
        customInsert (insertImg, result, editor) {
          let url = result.url
          insertImg(url)
        }
      }
      editor.create()
    }
  }
</script>

<template>
  <div class="">
    <div id="editorElem" style="text-align:left; width: 460px;"></div>
    <button v-on:click="getContent">查看内容</button>
  </div>
</template>

<style lang="scss">
  #editorElem {
    .w-e-text {
      overflow-y: auto;
    }
    .w-e-text {
      ul {
        list-style-type: disc;
      }
      ol {
        list-style-type: decimal;
      }
    }
  }
</style>
