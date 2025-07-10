import 'bootstrap/dist/css/bootstrap.min.css';
import { createApp } from 'vue'
const app = createApp(App)
app.use(router)

import { createPinia } from 'pinia'
import persisted from "pinia-plugin-persistedstate";
const pinia = createPinia();
pinia.use(persisted);
app.use(pinia);


import App from './App.vue'
import router from './router'





app.mount('#app')
