<template>
  <el-select :clearable="clearable" :disabled="disabled" v-model="selectId" :placeholder="placeholder" @change="onSelectChange" :size="size" filterable>
    <el-option v-for="item in options" :key="item[valueKey]" :label="item[labelKey]" :value="item[valueKey]"></el-option>
  </el-select>
</template>

<script>
import request from '@/utils/request'

export default {
  name: "AjaxSelect",
  props: {
    value: {},
    disabled: { type: Boolean, default: false },
    clearable: { type: Boolean, default: true },
    size: { type: String, default: 'medium' },
    placeholder: { type: String, default: '请选择' },
    /** 当满足指定条件后才进行加载 */
    load: { type: Boolean, default: true },
    /** 选项列表的加载地址 */
    url: { type: String, default: null },
    /** 选项列表作为标签的属性 */
    labelKey: { type: String, default: 'name' },
    /** 选项列表作为值的属性 */
    valueKey: { type: String, default: 'id' },
  },
  data() {
    return {
      selected: {}, selectId: null, options: []
    }
  },
  created() {
    this.loadData()
  },
  mounted() {
    this.selectId = this.value;
  },
  watch: {
    value(val) {
      this.selectId = val;
    },
    url() {
      this.loadData()
    },
    selectId(selectId) {
      this.selected = this.options.find(o => o[this.valueKey] === selectId);
    }
  },
  methods: {
    onSelectChange(selectId) {
      this.$emit('input', this.selectId);
      this.$emit('change', {
        id: selectId,
        row: this.options.find(o => o[this.valueKey] === selectId)
      });
    },
    loadData() {
      if (!this.url || !this.load) return;
      request({ url: this.url, method: 'get' }).then(res => {
        this.options = res.data ? res.data : [{ [this.labelKey]: "加载数据失败" }];
      })
    }
  },

}
</script>

<style scoped>
</style>
