<template>
  <el-container style="margin: 0; padding: 0; width: 100%; height: 100%">
    <el-header id="nav">
      <Header />
    </el-header>
    <el-main class="div-content">
      <div class="login-form-shade"></div>
      <el-form class="login-form">
        <el-form-item class="form-item">
          <h1 style="color: white; padding-top: 20px">管理员登录</h1>
        </el-form-item>
        <!-- <el-form-item class="form-item" prop="userId">
          <el-input
            class="input-item"
            v-model="loginData.userId"
            placeholder="请输入管理员ID"
            maxlength="32"
            show-word-limit
          ></el-input>
        </el-form-item> -->
        <el-form-item class="form-item">
          <el-input
            class="input-item"
            v-model="loginPwd"
            placeholder="请输入管理员密码"
            show-password
            @keyup.enter.native="login"
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button class="login-btn" type="primary" @click="login">
            <pre class="font-family-item">登&#12288;录</pre>
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>
<script>
import Header from "@/components/Header/Header.vue";
export default {
  components: { Header },
  name: "App",
  components: {
    Header,
  },
  data() {
    return {
      loginPwd: ""
    };
  },
  methods: {
    toSignUp() {
      this.changeItem();
    },
    toLogin() {
      this.changeItem();
    },
    login() {
      var self = this;
      const loading = self.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      var url = self.$api.LoginAdmin.url;
      self.$api.LoginAdmin.url = url.split("=")[0] + "=" + self.loginPwd;
      self.$http.post(self.$api.LoginAdmin).then((res) => {
        if (res.data.statusCode == self.$resultCode.SUCCESS.code) {
          self.$message.success("登录成功");
          let admin = {
            id: 1,
            userId: admin,
            roleId: 0
          }
          self.$store.commit("login", admin);
          loading.close();
          self.$router.replace({ path: "/center/admin" });
        } else {
          if (
            res.data.statusCode == self.$resultCode.USER_CREDENTIALS_ERROR.code
          ) {
            self.$message.warning("登录失败，请检查管理员密码是否正确");
          } else {
            self.$message.error("登录失败，未知错误！");
          }
          loading.close();
        }
      });
    },
  },
};
</script>
<style>
* {
  margin: 0px;
  padding: 0px;
}
.login-form {
  border-radius: 15px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgb(15, 10, 38);
  width: 420px;
  height: 280px;
  box-shadow: 2px 2px 15px rgb(255, 255, 255);
}
.input-item {
  margin-top: 32px;
  width: 320px !important;
  box-shadow: 2px 2px 15px rgb(89, 186, 224);
}
.form-item {
  height: 50px;
  
}
.font-family-item {
  font-family: dengxian;
}
.login-btn {
  font-size: 1.5em !important;
  margin-top: 48px;
  width: 120px;
  height: 40px;
}
.div-content {
  height: 84vh;
  /* background-image: linear-gradient(to right , #04082c,#0b0d8f, #190342); */
  background-image: url("../../images/loginadmin.png");
  background-size: cover;
}
</style>