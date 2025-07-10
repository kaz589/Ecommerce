class JwtUtils {
  /**
   * 解碼 JWT Token 的 Payload 部分
   * @param {string} token - JWT Token
   * @returns {Object} 解碼後的 Payload 資料
   */
  static decodeToken(token) {
    try {
      if (!token) {
        throw new Error("Token is missing");
      }

      // 獲取 JWT 的 Payload 部分（第二段）
      const payload = token.split(".")[1];

      // 將 Base64 URL 安全編碼解碼為字串
      const decodedPayload = decodeURIComponent(
        atob(payload.replace(/-/g, "+").replace(/_/g, "/"))
          .split("")
          .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
          .join("")
      );

      // 將解碼後的字串轉換為 JSON
      return JSON.parse(decodedPayload);
    } catch (error) {
      console.error("Failed to decode token:", error.message);
      return {};
    }
  }

  /**
   * 檢查 JWT Token 是否過期
   * @param {string} token - JWT Token
   * @returns {boolean} 是否過期
   */
  static isTokenExpired(token) {
    try {
      const decoded = this.decodeToken(token);
      const currentTime = Math.floor(Date.now() / 1000); // 獲取當前時間（秒）
      return decoded.exp && decoded.exp < currentTime; // 如果 exp 存在且小於當前時間，則已過期
    } catch (error) {
      console.error("Failed to check token expiration:", error.message);
      return true; // 如果解析失敗，默認認為已過期
    }
  }

  /**
   * 從 Token 中提取角色資訊
   * @param {string} token - JWT Token
   * @returns {Array} 角色資訊
   */
  static getRolesFromToken(token) {
    try {
      const decoded = this.decodeToken(token);
      return decoded.roles || []; // 返回 roles 欄位，默認為空陣列
    } catch (error) {
      console.error("Failed to extract roles from token:", error.message);
      return [];
    }
  }
}
export default JwtUtils;