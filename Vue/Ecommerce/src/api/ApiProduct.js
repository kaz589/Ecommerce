import instance from "@/utils/http";

export const ApiProduct = {
   //分頁搜尋測試
  findAllUsers: () => instance.get(`/product/All`),
 /**
   * 購買商品 API
   * @param {Object} data - 購買商品所需資料
   * @param {number} data.productId - 商品 ID
   * @param {number} data.quantity - 購買商品的數量
   * @returns {Promise} - 回傳 API 回應的 Promise
   */
  
  purchase: (data) => instance.post("/product/purchase", data),
  /**
   * 新增商品 API
   * @param {Object} data - 新增商品所需資料
   * @param {string} data.name - 商品名稱
   * @param {string} data.description - 商品描述
   * @param {number} data.categoryId - 商品分類 ID
   * @param {number} data.price - 商品價格
   * @param {number} data.stockQuantity - 商品庫存數量
   * @param {string} data.status - 商品狀態 (例如: 上架/下架)
   * @returns {Promise} - 回傳 API 回應的 Promise
   */
  create: (data) => instance.post("/product/create", data),
}