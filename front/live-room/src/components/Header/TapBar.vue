<template>
  <div id="nav">
    <div v-html="userId" style="font-weight: bold;margin-top: 20px"></div>

    <el-dropdown class="user-dropdown" @command="handleCommon">
      <div
        class="user-photo"
        :class="courseList_photo_color[Math.round(Math.random() * 7)]"
      >
        <p class="user-photo-font">{{ userId.substring(0, 1) }}</p>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="personCenter">个人中心</el-dropdown-item>
        <el-dropdown-item command="backToHome">返回主页</el-dropdown-item>
        <el-dropdown-item command="changeUser">更换账号</el-dropdown-item>
        <!-- <el-dropdown-item disabled>双皮奶</el-dropdown-item> -->
        <el-dropdown-item command="exit" divided>退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      userId: "",
      courseList_photo_color: [],
    };
  },
  mounted() {
    var self = this;
    self.courseList_photo_color = self.$photoColor.colorList;
    self.userId = self.$store.state.userId;
  },
  methods: {
    handleCommon(command){
      var self = this;
      switch(command){
        case "personCenter":
          self.$message.success('进入个人中心');
          if(self.$store.state.roleId == 1){
            self.$router.push({name: "CenterTeacher"});
          }else {
            self.$router.push({name: "CenterStudent"});
          }
          break;
        case "backToHome":
          if(window.location.href.split("#")[1] != '/'){
            self.$router.push({name: "Home"});
          }
          break;
        case "changeUser":
          self.$message.success("更换其他账户");
          self.$store.commit("logout");
          self.$router.push({name: "Login"});
          break;
        case "exit":
          self.$message.success('退出登录');
          self.$store.commit("logout");
          self.$router.push({name: 'Home'})
          window.location.reload();
          break;
      }
      
    }
  }
};
</script>

<style>
.user-dropdown {
  position: relative;
  float: right;
  right: 8vw;
  top: -8.5vh;
}
.user-photo:hover {
  box-shadow: 0px 0px 15px white;
}
.user-photo {
  margin-top: 4.2vh;
  cursor: pointer;
  width: 3em;
  height: 3em;
  border-radius: 50%;
  outline: 0;
  display:flex; justify-content:center; align-items:center;
}
.user-photo-font {
  font-size: 2em;
  color: white;
}
</style>