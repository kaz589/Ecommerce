import 'bootstrap/dist/css/bootstrap.min.css';
import { createApp } from 'vue'
const app = createApp(App)
app.use(router)

import { createPinia } from 'pinia'
app.use(createPinia())

import App from './App.vue'
import router from './router'





app.mount('#app')
