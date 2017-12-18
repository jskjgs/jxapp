<script>
  /*
   * Created by zhengji
   * Date: 2017/10/18
   */
  import E from 'wangeditor'

  export default {
    name: 'editor',
    props: {
      value: String,
      uploadImgServer: '' // 图片文件上传地址
    },
    data () {
      return {
      }
    },
    methods: {
      getContent: function () {
        alert(this.editorContent)
      }
    },
    mounted () {
      let editor = new E(this.$refs.editorElem)
      // 菜单配置
      editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'italic',  // 斜体
        'underline',  // 下划线
        // 'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        // 'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'image',  // 插入图片
        'table'  // 表格
      ]
      // 监听函数
      editor.customConfig.onchange = (html) => {
        this.editorContent = html
      }
      /* 图片上传配置 */
      // 配置服务器端地址
      editor.customConfig.uploadImgServer = this.uploadImgServer || ''
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
    },
    computed: {
      editorContent: {
        get () {
          return this.value
        },
        set (content) {
          this.$emit('input', content)
        }
      }
    }
  }
</script>

<template>
  <div class="rich-text-editor">
    <div ref="editorElem"></div>
  </div>
</template>

<style lang="scss">
  .rich-text-editor {
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
