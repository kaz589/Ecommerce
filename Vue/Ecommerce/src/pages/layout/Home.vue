<template>
  <div>
    <!-- 導覽列 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <!-- Logo -->
        <router-link class="navbar-brand" to="/">MyWebsite</router-link>
        
        <!-- 漢堡菜單按鈕 -->
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <!-- 導覽項目 -->
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item" v-for="link in navLinks" :key="link.name">
              <router-link
                class="nav-link"
                :to="link.href"
                :class="{ active: isActive(link.href) }"
              >
                {{ link.name }}
              </router-link>
            </li>
          </ul>

          <!-- 右上角登出按鈕 -->
          <button
            class="btn btn-outline-light"
            @click="logout"
          >
            登出
          </button>
        </div>
      </div>
    </nav>

    <!-- 路由視圖 -->
    <router-view></router-view>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router"; // 引入 useRouter
import { useAuthStore } from "@/stores/Auth";
const authStore = useAuthStore();

// 動態導覽項目
const navLinks = ref([
  { name: "首頁", href: "/" },
  { name: "查看用戶", href: "/users" },
  { name: "查看產品", href: "/products" },
]);

// 判斷當前路由是否為高亮狀態
const router  = useRouter();
const isActive = (href) => router .path === href;

// 登出功能
const logout = () => {
  authStore.logout();
  router.push("/login"); 
};
</script>

<style scoped>
/* 如果需要額外客製化樣式，可以在這裡定義 */
</style>
