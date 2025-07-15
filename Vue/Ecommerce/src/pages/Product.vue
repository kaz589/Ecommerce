<template>
  <div class="container">
    <!-- 上架按鈕 -->
    <button @click="showModal = true" class="btn btn-primary">上架</button>
    <br>
    
    <!-- 無產品資料提示 -->
    <div v-if="products.length === 0" class="empty-message">
      暫無產品資料
    </div>
    
    <!-- 產品卡片展示 -->
    <div
      v-for="(product, idx) in products"
      :key="product.id"
      class="product-card"
      style="width: 18rem; margin: 10px;"
    >
      <productCard :product="product"></productCard>
    </div>
    
    <!-- 彈出視窗 -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h2>新增商品</h2>
        <form @submit.prevent="submitProduct">
          <div class="form-group">
            <label for="name">商品名稱</label>
            <input
              type="text"
              id="name"
              v-model="newProduct.name"
              required
              placeholder="輸入商品名稱"
            />
          </div>
          <div class="form-group">
            <label for="description">商品描述</label>
            <textarea
              id="description"
              v-model="newProduct.description"
              placeholder="輸入商品描述"
            ></textarea>
          </div>
          <div class="form-group">
            <label for="categoryId">分類</label>
            <select
              id="categoryId"
              v-model="newProduct.categoryId"
              required
            >
              <option value="" disabled>請選擇分類</option>
              <option
                v-for="(category, index) in categories"
                :key="index"
                :value="index"
              >
                {{ category }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="price">價格</label>
            <input
              type="number"
              id="price"
              v-model="newProduct.price"
              required
              placeholder="輸入價格"
            />
          </div>
          <div class="form-group">
            <label for="stockQuantity">庫存數量</label>
            <input
              type="number"
              id="stockQuantity"
              v-model="newProduct.stockQuantity"
              required
              placeholder="輸入庫存數量"
            />
          </div>
          <div class="form-group">
            <label for="status">狀態</label>
            <input
              type="text"
              id="status"
              v-model="newProduct.status"
              required
              placeholder="輸入狀態 (例如: 上架/下架)"
            />
          </div>
          <div class="modal-buttons">
            <button type="submit" class="btn btn-success">提交</button>
            <button type="button" @click="closeModal" class="btn btn-secondary">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import productCard from "@/components/productCard.vue";
import { ref, onMounted } from "vue";
import { ApiProduct } from "@/api/ApiProduct";
import { ApiCategory } from "@/api/apiCategory";

const products = ref([]);
const categories = ref([]); // 儲存分類資料
const showModal = ref(false);

const newProduct = ref({
  name: "",
  description: "",
  categoryId: null,
  price: null,
  stockQuantity: null,
  status: "",
});

const fetchProducts = async () => {
  try {
    const response = await ApiProduct.findAllUsers();
    if (response.data && response.data.content) {
      products.value = response.data.content;
    } else {
      console.warn("API 回傳資料結構不符合預期:", response.data);
      products.value = [];
    }
  } catch (error) {
    console.error("Error fetching products:", error);
    products.value = [];
  }
};

const fetchCategories = async () => {
  try {
    const response = await ApiCategory.findDistinctCategory();
    if (response.data) {
      categories.value = response.data;
    } else {
      console.warn("API 回傳資料結構不符合預期:", response.data);
      categories.value = [];
    }
  } catch (error) {
    console.error("Error fetching categories:", error);
    categories.value = [];
  }
};

const submitProduct = async () => {
  try {
    console.log("Submitting new product:", newProduct.value);
    
    const response = await ApiProduct.create(newProduct.value);
    if (response.status === 200) {
      alert("商品新增成功！");
      fetchProducts(); // 重新載入商品資料
      closeModal();
    } else {
      alert("商品新增失敗，請檢查資料！");
    }
  } catch (error) {
    console.error("Error creating product:", error);
    alert("商品新增時發生錯誤！");
  }
};

const closeModal = () => {
  showModal.value = false;
  newProduct.value = {
    name: "",
    description: "",
    categoryId: null,
    price: null,
    stockQuantity: null,
    status: "",
  };
};

onMounted(() => {
  fetchProducts();
  fetchCategories(); // 載入分類資料
});
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 10px;
  height: 100vh;
  text-align: center;
}

.empty-message {
  font-size: 1.5rem;
  color: #999;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-success {
  background-color: #28a745;
  color: white;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}
</style>