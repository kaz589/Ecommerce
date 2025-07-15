
import instance from "@/utils/http";

export const ApiAuth = {
   /**
   * 登入 API
   * @param {Object} data - 登入所需的資料
   * @param {string} data.username - 使用者名稱
   * @param {string} data.password - 使用者密碼
   * @returns {Promise} - 回傳 API 回應的 Promise
   */
  login: (data) => instance.post("/auth/login", data, {
      headers: {
        "Content-Type": "application/json", // 確保請求頭正確
      },
    }),
}