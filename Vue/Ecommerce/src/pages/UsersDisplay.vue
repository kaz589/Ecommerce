<template>
   <div class="container mt-5">
    <h1 class="text-center">Users List</h1>
    <table class="table table-bordered table-striped mt-4">
      <thead class="table-dark">
        <tr>
          <th>UserId</th>
          <th>Username</th>
          <th>Email</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.userId">
          <td>{{ user.userId }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ApiAirport } from "@/api/ApiUsers"; // 引入 API 模組

const users = ref([]);

const fetchUsers = async () => {
  try {
    const response = await ApiAirport.findAllUsers(); // 使用 ApiAirport 方法
    users.value = response.data.content; // 假設 API 返回的數據格式包含 content 屬性
  } catch (error) {
    console.error("Error fetching users:", error);
  }
};

onMounted(() => {
  fetchUsers();
});
</script>

<style  scoped>

</style>