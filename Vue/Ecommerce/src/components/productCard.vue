<template>
  <div class="card" style="width: 18rem;height:30rem;">
    <img :src="url" class="card-img-top" alt="..." />
    <div class="card-body">
      <h5 class="card-title">{{product.name}}</h5>
      <p class="card-text">{{product.description}}</p>
      <p class="card-text">庫存:{{product.stockQuantity}}</p>
      
      <input type="number" v-model="numbuy"  min="1" max="5">
      <br>
      <p class="card-text">價格:{{product.price}}</p>
       <button @click="buy" class="btn btn-primary">購買</button>
    </div>
  </div>
</template>

<script setup>

const props = defineProps(["product"]);
import { ApiProduct } from "@/api/ApiProduct.js";
import { ref,computed,onMounted } from "vue";
import { useAuthStore } from "@/stores/Auth";
const authStore = useAuthStore();
const numbuy = ref(1);
const url=ref("https://dummyimage.com/600x400/000/fff");
const buyproduct = computed(() => {
  return {
   
    
     productId: props.product.productId,
    quantity: numbuy.value
  };
});

const buy = () => {
  
  
  if (numbuy.value > props.product.stockQuantity) {
    alert("購買數量超過庫存");
    return;
  }
  console.log("購買商品:", buyproduct.value);
  
  // 假設有一個購買API
  ApiProduct.purchase(buyproduct.value)
    .then(response => {
      console.log("購買成功:", response.data);
      
      alert("購買成功");
      // 更新庫存數量
      //props.product.stockQuantity -= numbuy.value;
    })
    .catch(error => {

      if (error.response) {
      const message = error.response.data.message;
      console.log("購買失敗:", error);
      
      alert(`錯誤: ${message}`);
    } else {
      alert('系統錯誤，請稍後再試');
    }
      // console.error("購買失敗:", error);
      // alert("購買失敗，請稍後再試");
    });
};

</script>

<style scoped></style>
