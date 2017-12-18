<script>
/*
 * Created by zhengji
 * Date: 2017/10/20
 */
export default {
  name: 'Info',
  props: {
    data: {
      type: Object
    }
  },
  data () {
    this.dataList = [{
      key: 'name',
      label: '姓名'
    }, {
      key: 'phone',
      label: '手机号'
    }, {
      key: 'age',
      label: '年龄'
    }, {
      key: 'id',
      label: '用户ID'
    }]
    return {
    }
  }
}
</script>

<template>
  <div>
    <template v-if="data">
      <el-row
        class="data-item flex"
        v-for="item in dataList"
        :key="item.label">
        <span class="flex-item--none data-item__label">{{ item.label }}</span>
        <span class="colon">:</span>
        <span class="flex-item data-item__value">
          {{ data[item.key] || '--' }}
        </span>
      </el-row>
    </template>
    <div v-else>
      暂无数据
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .data-item {
    & + .data-item {
      margin-top: 20px;
    }
    &__label {
      color: #b2b2b2;
      width: 70px;
    }
    .colon {
      color: #b2b2b2;
      margin-right: 20px;
    }
    &__value {
    }
  }
</style>
