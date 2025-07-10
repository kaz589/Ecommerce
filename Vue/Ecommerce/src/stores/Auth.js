import { defineStore } from "pinia";
import JwtUtils from "@/utils/JwtUtils";
import { hasRole, cleanRole } from "@/utils/role";

export const useAuthStore = defineStore(
  "auth",
  {
    state: () => ({
      token: localStorage.getItem("token") || "",
      user: null, // 存儲用戶信息
      isAuthenticated: false, // 記錄用戶是否登入
      roles: JSON.parse(localStorage.getItem("roles")) || "", // 存儲用戶角色
    }),
    actions: {
      login(token) {
        try {
          const payload = JwtUtils.decodeToken(token);

          if (!payload || !payload.sub) {
            throw new Error("無效的 Token，無法提取用戶信息");
          }

          this.user = payload.sub; // 確保 `sub` 存在
          this.token = token;
          this.roles = cleanRole(payload); // 使用 cleanRole 函數來清理角色
          this.isAuthenticated = true; // 設置為登入狀態

          // 將 token 和角色存入 localStorage
          localStorage.setItem("token", token);
          localStorage.setItem("roles", JSON.stringify(this.roles));
        } catch (error) {
          console.error("登入失敗:", error.message);
          throw new Error("登入過程中出現錯誤，請檢查 Token 是否正確");
        }
      },
      logout() {
        this.isAuthenticated = false; // 設置為未登入狀態
        this.user = null;
        this.token = "";
        this.roles = [];
        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        localStorage.removeItem("roles");
      },
      checkLoginStatus() {
        // 檢查 localStorage 中是否有保存登入狀態
        const isLoggedIn = localStorage.getItem("isLoggedIn");
        const user = JSON.parse(localStorage.getItem("user"));
        const roles = JSON.parse(localStorage.getItem("roles") || "[]");

        if (isLoggedIn === "true" && user) {
          this.isAuthenticated = true;
          this.user = user;
          this.roles = roles;
        } else {
          this.isAuthenticated = false;
          this.user = null;
          this.roles = [];
        }
      },
      // 🔍 加一個方法來判斷是否有某個角色
      hasRole(role) {
        console.log("當前角色", role);

        return this.roles.includes(role);
      },
      // 加一個方法來即時更新角色權限
      updateAuthorityDetail(newAuthorityDetail) {
        if (this.user) {
          this.user.authorityDetail = newAuthorityDetail;
        }
      },
      // 加一個方法來即時更新角色
      updateAuthority(newAuthority) {
        if (this.user) {
          this.user.authority = newAuthority;
        }
      },
    },
  },
  {
    persist: true,
  }
);
