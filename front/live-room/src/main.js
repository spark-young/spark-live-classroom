import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import VueAliplayerV2 from 'vue-aliplayer-v2';
import './common/font/font.css'
import "./assets/style/global.css";
import api from "./http/api";
import http from "./http/http";
import resultCode from "./http/resultCode";
import photoColor from "@/assets/js/photoColor"

import adapter from 'webrtc-adapter';

import  VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

// 对后端接口 进行全局注册，将api挂载到vue的原型上
Vue.prototype.$api = api;
// 对请求方式 进行全局注册
Vue.prototype.$http = http;
// Vue.prototype.$webhttp = webhttp;
Vue.prototype.$resultCode = resultCode;
Vue.prototype.$isLogin = false;
Vue.prototype.$photoColor = photoColor;
Vue.config.productionTip = false
Vue.use(VueAliplayerV2)
Vue.use(ElementUI);
Vue.use(VueQuillEditor)
Vue.use(adapter)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
