<template>
  <el-container style="margin: 0; padding: 0; width: 100%; height: 100%">
    <el-header id="nav">
      <Header />
    </el-header>
    <el-main class="div-content">
      <div class="login-form-shade"></div>
      <el-form class="login-form" :model="loginData" v-show="isLogin">
        <el-form-item class="form-item">
          <h1 style="color: white;padding-top: 20px">登&nbsp;&nbsp;录</h1>
        </el-form-item>
        <el-form-item class="form-item" prop="userId">
          <el-input
            class="input-item"
            v-model="loginData.userId"
            placeholder="请输入用户ID"
            maxlength="32"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item" prop="loginPwd">
          <el-input
            class="input-item"
            v-model="loginData.loginPwd"
            placeholder="请输入密码"
            show-password
            @keyup.enter.native="login"
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button class="login-btn" type="primary" @click="login">
            <pre class="font-family-item">登&#12288;录</pre>
          </el-button>
          <el-button class="login-btn" type="success" @click="toSignUp">
            <pre class="font-family-item">去注册</pre>
          </el-button>
        </el-form-item>
      </el-form>
      <el-form class="signUp-form" :model="signUpData" v-show="isSignUp">
        <el-form-item class="form-item">
          <h1 style="color: white;padding-top: 20px">注&nbsp;&nbsp;册</h1>
        </el-form-item>
        <el-form-item class="form-item" prop="userId">
          <el-input
            class="input-item"
            v-model="signUpData.userId"
            placeholder="请输入用户ID"
            maxlength="32"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item" prop="loginPwd">
          <el-input
            class="input-item"
            v-model="signUpData.loginPwd"
            placeholder="请输入密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item" prop="surePwd">
          <el-input
            class="input-item"
            v-model="signUpData.surePwd"
            placeholder="再次确认密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-switch
          v-model="signUpData.role"
          active-text="教师"
          inactive-text="学生"
          inactive-color="#fff"
          style="margin-bottom: 20px;"
        >
        </el-switch>
        <!-- <el-form-item prop="role">
          <template>
            <el-radio v-model="signUpData.role" label="1" border>老师</el-radio>
            <el-radio v-model="signUpData.role" label="2" border>学生</el-radio>
          </template>
        </el-form-item> -->
        <el-form-item class="form-item">
          <el-button class="login-btn" type="primary" @click="signUp" style="width: 120px">
            <pre class="font-family-item">注&#12288;册</pre>
          </el-button>
          <el-button class="login-btn" type="success" @click="toLogin">
            <pre class="font-family-item">回登录</pre>
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>
  <!-- <div class="login-content">
    <div v-if="loginRoom" class="login-item">
      <div>
    <el-button @click="$router.push('/room/teacher?ijt='+new Date().getTime())" class="login-item-button button-create">创建直播间</el-button>
    </div>
    <div>
      <el-button @click="onAddRoom()" class="login-item-button button-add">加入直播间</el-button>
    </div>
    </div>
    <div v-if="addRoom" class="login-item">
      <el-input
        type="text"
        v-model="uuid"
        placeholder="请输入直播间id"
        class="login-item-input uuid-input"
      />
      <el-button @click="onRoomIn()" class="login-item-button button-in"
        >进&nbsp;&nbsp;入</el-button
      >
      <a href="" class="login-item-a a-back">←返&nbsp;&nbsp;&nbsp;&nbsp;回</a>
    </div>
  </div> -->
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
      isLogin: true,
      isSignUp: false,
      loginData: {
        userId: "",
        loginPwd: "",
      },
      signUpData: {
        userId: "",
        loginPwd: "",
        surePwd: "",
        role: true,
      },
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
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        // setTimeout(() => {
        //   loading.close();
        // }, 2000);
      self.$http.post(self.$api.Login, self.loginData).then((res) => {
        console.log(res.data);
        if (res.data.statusCode == self.$resultCode.SUCCESS.code) {
          self.$message.success("登录成功");
          self.$store.commit("login", res.data.data);
          loading.close();
          switch (res.data.data.roleId) {
            case 0:
              break;
            case 1:
              self.$router.replace({ path: "/center/teacher" });
              break;
            case 2:
              self.$router.replace({ path: "/center/student" });
              break;
          }
        } else {
          if (
            res.data.statusCode == self.$resultCode.USER_ACCOUNT_NOT_EXIST.code
          ) {
            self.$message.warning("登录失败，用户名不存在");
          } else if (
            res.data.statusCode == self.$resultCode.USER_CREDENTIALS_ERROR.code
          ) {
            self.$message.warning("登录失败，请检查用户名和密码是否匹配");
          } else if(res.data.statusCode == self.$resultCode.USER_IS_UNACTIVED.code){
            self.$message.warning(res.data.message);
          }else{
            self.$message.error("登录失败，未知错误！");
          }
        loading.close();
        }
      });
    },
    signUp() {
      var self = this;
      if (self.signUpData.loginPwd != self.signUpData.surePwd) {
        self.$message.warning("两次密码不一致，请确认！");
        return;
      }
      let signUpDTO = {
        userId: self.signUpData.userId,
        loginPwd: self.signUpData.loginPwd, 
        role: self.signUpData.role?1:2,
      }
      self.$http.post(self.$api.SignUp, signUpDTO).then((res) => {
        if (res.data.statusCode == self.$resultCode.SUCCESS.code) {
          self.$message.success("注册成功，请登录！");
          self.toLogin();
        } else {
          if (
            res.data.statusCode ==
            self.$resultCode.USER_ACCOUNT_ALREADY_EXIST.code
          ) {
            self.$message.warning("该用户名已存在");
          } else {
            self.$message.error("未知错误");
          }
          self.resetSignUpData();
        }
      });
    },
    resetSignUpData() {
      var self = this;
      self.signUpData.userId = "";
      self.signUpData.loginPwd = "";
      self.signUpData.surePwd = "";
      self.signUpData.role = true;
    },
    changeItem() {
      this.isLogin = !this.isLogin;
      this.isSignUp = !this.isSignUp;
    },
    onAddRoom() {
      this.loginRoom = false;
      this.addRoom = true;
    },
    onRoomIn() {
      //这里将请求后台验证uuid，存在则进入直播页面，不存在则弹窗提示
      if (this.uuid.length == 0) {
        alert("请输入直播间id");
      } else {
        this.$router.push("/room/student?ijt=" + this.uuid);
      }
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
  box-shadow: 2px 2px 15px rgb(0, 0, 0);
}
.signUp-form {
  border-radius: 5px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgb(32, 34, 38);
  width: 420px;
  height: 400px;
  box-shadow: 2px 2px 15px rgb(0, 0, 0);
}
.input-item {
  /* margin-top: 4px; */
  width: 320px !important;
  box-shadow: 2px 2px 15px rgb(89, 186, 224);
}
.form-item{
  height: 50px;
}
.font-family-item {
  font-family: dengxian;
}
.login-btn{
  font-size: 1.5em !important;
  width: 120px;
  height: 40px;
}
.div-content {
  height: 84vh;
  background-image: url("../../images/login.jpg"); 
  background-size: cover;
}
</style>