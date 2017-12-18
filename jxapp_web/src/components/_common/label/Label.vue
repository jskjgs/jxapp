<script>
  // 封装组件
  import SortItem from './SortItem'

  export default {
    name: 'EnterpriseLabel',
    components: {
      SortItem
    },
    props: {
      sorts: Array, // 分类数组数据
      type: {
        type: String,
        default: 'use' // configure / use
      },
      chosen: Object // 勾选的标签（用于type: use）{ [分类名id]: '标签id'/ ['标签id'] }
    },
    data () {
      return {
      }
    },
    computed: {
      chosenProxy: {
        get () {
          return this.chosen || {}
        },
        set (val) {
          this.$emit('update:chosen', val)
        }
      }
    },
    methods: {
      // 监听分类删除
      handleSortDel ({ sort, index }, resolve) {
        this.$emit('del', { sort, index }, resolve)
      },
      // 添加分类
      addSort () {
        this.sorts.push({
          new: true,
          title: {
            name: ''
          },
          attribute: 1, // 1单选, 2多选
          labelInfoList: []
        })
      },
      // 监听分类更新
      handleUpdate ({ index, sort }, resolve) {
        if (sort.new) {
          delete sort.new
          this.$emit('create', { index, sort }, resolve)
        } else {
          this.$emit('update', { index, sort }, resolve)
        }
      }
    }
  }
</script>

<template>
  <div class="enterprise-label">
    <el-button
      v-if="type === 'configure'"
      type="primary"
      @click="addSort">
      添加分类
    </el-button>
    <sort-item
      style="margin-top: 20px"
      v-for="(sort, index) in sorts"
      :key="sort.title.id"
      :chosen.sync="chosenProxy"
      :type="type"
      :sort="sort"
      :index="index"
      @delete="handleSortDel"
      @update="handleUpdate">
    </sort-item>
  </div>
</template>

<style lang="scss">
</style>
