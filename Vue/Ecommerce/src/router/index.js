import { createRouter, createWebHistory } from 'vue-router'
import UsersDisplay from '@/pages/UsersDisplay.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: UsersDisplay,
    },
   
  ],
})

export default router
