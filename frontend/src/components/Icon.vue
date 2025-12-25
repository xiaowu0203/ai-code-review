<template>
  <el-icon>
    <component :is="iconComponent" />
  </el-icon>
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'
import { getIconComponent } from '@/utils/icons'

const props = defineProps({
  name: {
    type: String,
    required: true
  }
})

const iconComponent = computed(() => {
  return getIconComponent(props.name)
})
</script>