<template>
     <div class="container">
    
    
         <productCard></productCard>
  </div>

</template>

<script setup>
import productCard from '@/components/productCard.vue';

</script>

<style  scoped>
/* 置中樣式 */
.container {
  display: flex;
  justify-content: center; /* 水平置中 */
  align-items: center;    /* 垂直置中 */
  height: 100vh;          /* 讓容器高度充滿視窗 */
  text-align: center;     /* 文字置中 */
}
</style>