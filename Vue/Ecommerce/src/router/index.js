import { createRouter, createWebHistory } from 'vue-router'
import UsersDisplay from '@/pages/UsersDisplay.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      
      component: () => import("@/pages/layout/Home.vue"),
      children: [
        {
          path: "",
          name: "home", // 將名稱移到子路由
          component: () => import("@/pages/showroles.vue"),
        },
        {
          path: 'users',
          name: 'users',
          component: ()=> import("@/pages/UsersDisplay.vue"),
        },
        {
          path: 'products',
          name: 'products',
          component: () => import("@/pages/Product.vue"),
        },
      ]
    },
     {
      path: '/login',
      name: 'login',
      component: () => import("@/pages/Login.vue")
    },
   
  ],
})

export default router
