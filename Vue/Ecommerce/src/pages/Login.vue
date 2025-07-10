<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-4">
        <h2 class="text-center mb-4">登入</h2>
        <form @submit.prevent="handleLogin">
          <!-- 帳號輸入 -->
          <div class="mb-3">
            <label for="username" class="form-label">帳號</label>
            <input
              type="text"
              id="username"
              class="form-control"
              v-model="username"
              placeholder="請輸入帳號"
              autocomplete="off"
              required
            />
          </div>
          <!-- 密碼輸入 -->
          <div class="mb-3">
            <label for="password" class="form-label">密碼</label>
            <input
              type="password"
              id="password"
              class="form-control"
              v-model="password"
              placeholder="請輸入密碼"
              autocomplete="off"
             
              required
            />
          </div>
          <!-- 錯誤訊息 -->
          <div v-if="errorMessage" class="alert alert-danger">
            {{ errorMessage }}
          </div>
          <!-- 登入按鈕 -->
          <button type="submit" class="btn btn-primary w-100">登入</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ApiAuth } from "@/api/ApiAuth";
import { useAuthStore } from "@/stores/Auth";
import { useRouter } from "vue-router";
const router = useRouter();

// 定義狀態
const username = ref("Test1");
const password = ref("pkpk0912");
const errorMessage = ref("");

// 登入處理函數
const handleLogin = async () => {
  // 簡易驗證邏輯
  if (username.value === "" || password.value === "") {
    errorMessage.value = "帳號或密碼不可空白";
    return;
  }

  try {
    // 呼叫 API 方法並傳入資料
    const response = await ApiAuth.login({
      username: username.value,
      password: password.value,
    });
    console.log("登入成功:", response.data);
    const token= response.data.token;
    const authStore = useAuthStore();
   authStore.login(token);
    
    errorMessage.value = "";
    // 重置表單
    username.value = "";
    password.value = "";
    //登錄後跳轉到指定頁面;
          router.push("/");
  } catch (error) {
    console.error("登入失敗:", error);
    errorMessage.value = "帳號或密碼錯誤";
  }
};
</script>

<style scoped>
/* 可加入自訂樣式 */
</style>