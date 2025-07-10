<template>
    <div class="container">
    <h1>持有權限: {{ role }}</h1>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/Auth";
const authStore = useAuthStore();
const role = ref("");
if (authStore.isAuthenticated) {
    const roles = authStore.roles;
    role.value = roles ? roles.join(", ") : "No roles assigned";
} else {
    role.value = "User not authenticated";
}
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

h1 {
  color: #333;
  font-size: 1.5rem;
}
</style>