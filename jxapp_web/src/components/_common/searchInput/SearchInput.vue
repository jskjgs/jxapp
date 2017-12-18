<script>
export default {
  name: 'SearchInput',
  props: {
    placeholder: String,
    label: {
      type: String,
      default: '查询条件'
    }
  },
  data () {
    return {
      keyword: ''
    }
  },
  methods: {
    search () {
      this.$emit('search', this.keyword)
    },
    clearInput (emit) {
      this.keyword = ''
      if (emit !== false) {
        this.$nextTick(() => {
          this.$emit('search', this.keyword)
        })
      }
    }
  }
}
</script>

<template>
  <div class="search-input-wrap flex--center">
    <label style="margin-right: 20px;" class="flex--vcenter">
      {{ label }}：
      <el-input
        :placeholder="placeholder"
        class="inline-block search-input"
        v-model.trim="keyword">
      </el-input>
    </label>
    <el-button
      type="primary"
      @click="search">搜索</el-button>
    <el-button
      @click="clearInput">清空</el-button>
  </div>
</template>

<style lang="scss">
  .search-input {
    width: 320px;
  }
</style>
