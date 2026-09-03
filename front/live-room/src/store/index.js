import Vue from 'vue'
import Vuex from 'vuex'
import VueCookie from 'vue-cookie'
Vue.use(Vuex)
Vue.use(VueCookie)
export default new Vuex.Store({
  state: {
    id: Vue.cookie.get('id') == null? 0:Vue.cookie.get('id'),
    userId: Vue.cookie.get('userId') == null? 'none':Vue.cookie.get('userId'),
    roleId: Vue.cookie.get('roleId') == null? 0:Vue.cookie.get('roleId')
  },
  mutations: {
    login(state,data) {
      state.id = data.id;
      state.userId = data.userId;
      state.roleId = data.roleId;
      Vue.cookie.set('id',state.id,{ expire: '20m'});
      Vue.cookie.set('userId',state.userId,{ expire: '20m'});
      Vue.cookie.set('roleId',state.roleId,{ expire: '20m'});
    },
    logout(state){
      state.id = 0;
      state.userId = '';
      state.roleId = 0;
      Vue.cookie.delete('id');
      Vue.cookie.delete('name');
      Vue.cookie.delete('roleId');
    }
  },
  actions: {
  },
  modules: {
  }
})
