<script>
/*
 * Created by zhengji
 * Date: 2017/10/29
 */
export default {
  name: 'HeaderSearchSelect',
  props: {
    label: { // 表头文字
      type: String
    },
    width: {
      type: Number,
      default: 200
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    options: { // select选项
      type: Array,
      default () {
        return []
      }
    },
    value: { // select的值
      type: null
    }
  },
  data () {
    return {
      popoverVisible: false
    }
  },
  computed: {
    selectVal: {
      get () {
        return this.value
      },
      set (val) {
        this.popoverVisible = false
        this.$emit('input', val)
      }
    }
  },
  methods: {
    handlePopoverVisibleChange (visible) {
      this.$emit('visibleChange', visible)
    }
  }
}
</script>

<template>
  <div class="header-search-select">
    <el-popover
      ref="popover"
      popper-class="header-search-select__popover"
      placement="bottom"
      :width="width"
      trigger="click"
      v-model="popoverVisible"
      @show="handlePopoverVisibleChange(true)"
      @hide="handlePopoverVisibleChange(false)">
      <el-select v-model="selectVal" filterable :placeholder="placeholder">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-popover>
    <el-button type="text" v-popover:popover>
      {{ label }}<i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
    </el-button>
  </div>
</template>

<style lang="scss">
  .header-search-select {
    .el-button--text {
      color: inherit;
    }
  }
  .header-search-select__popover {
    padding: 0;
    .el-select {
      width: 100%;
    }
    .el-input__inner {
      border: 0;
    }
  }
</style>
