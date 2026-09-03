<template>
  <div>
    <Header v-show="unlogined" />
    
    <el-button
      v-show="unlogined"
      type="success"
      @click="toLogin"
      style="
        position: absolute;
        top: 2vh;
        right: 10vw;
        border: none;
        background: rgb(196, 90, 0);
      "
      >登录</el-button
    >
    <TapBar v-show="logined" />
  </div>
</template>

<script>
import Header from "@/components/Header/Header.vue";
import TapBar from "@/components/Header/TapBar.vue";
export default {
  name: "App",
  components: {
    Header,
    TapBar
  },
  data() {
    return {
      logined: this.$store.state.id != 0,
      unlogined: this.$store.state.id == 0,
    };
  },
  methods: {
    toLogin() {
      this.$router.push({name: 'Login'});
    },
  },
};
</script>