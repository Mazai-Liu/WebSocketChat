import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import axios from 'axios'
import bootstrap from 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$router = router;
app.config.globalProperties.$github_url = "https://cdn.jsdelivr.net/gh/Mazai-Liu/pictures@main/";
app.config.globalProperties.$api_token = "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";


app.use(router).use(bootstrap).mount('#app')
