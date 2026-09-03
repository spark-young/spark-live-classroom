<template>
  <el-container>
    <el-header id="nav">
      <MainHeader />
    </el-header>
    <el-container
      class="main-background"
      style="min-height: 81vh; display: flex"
    >
      <el-aside id="category">
        <div class="category-item">
          <CategoryList />
        </div>
      </el-aside>
      <el-main id="course">
        <div class="course-item">
          <CourseList />
        </div>
      </el-main>
      <el-aside id="other" style="width: 30vw">
        <h2 class="other-item">
          <div style="display: flex;flex-direction: row;margin-top: 2vh">
        <el-input v-model="searchContent" style="width: 80% !important;" placeholder="搜素课程" @keydown.enter.native="searchClass"></el-input>
        <el-button type="primary" icon="el-icon-search" style="background: rgb(204, 109, 0)" @click="searchClass()"></el-button>
          </div>
          <br/>
          {{todayClassCount == 0 ? "您今天暂时没有课程需要参加哦！" : "您今天共有"+todayClassCount+"个课程！"}}
        </h2>
      </el-aside>
    </el-container>
  </el-container>
</template>

<script>
import MainHeader from "@/components/Header/MainHeader.vue";
import CategoryList from "@/components/category/categoryList.vue";
import CourseList from "@/components/course/courseList.vue";
import Vue from "vue";
export default {
  name: "App",
  components: {
    MainHeader,
    CategoryList,
    CourseList,
  },
  data() {
    return {
      logined: this.$store.state.id != 0,
      unlogined: this.$store.state.id == 0,
      todayClassCount: 0,
      searchContent: "",
    };
  },
  mounted() {
    var self = this;
    if(self.$store.state.id != 0){
      var url = self.$api.ScheduleTodayClassCount.url;
      self.$api.ScheduleTodayClassCount.url = url.split("=")[0] + "=" + self.$store.state.id;
      self.$http.get(self.$api.ScheduleTodayClassCount).then((res) => {
        self.todayClassCount = JSON.stringify(res.data.data);
      });
    }
  },
  methods: {
    searchClass() {
      var self = this;
      console.log("search");
      if (window.location.href.split("=")[1] != self.searchContent){
        self.$router.push("/course/search?content=" + self.searchContent);
        console.log("search to "+self.searchContent);
      }
    }
  },
};
</script>
<style>
* {
  margin: 0px;
  padding: 0px;
}
.main-background {
  background-image: url("../images/login.jpg");
  background-size: 100vw 100vh;
}
#category {
  padding-left: 10vw;
  width: 25vw !important;
  background: transparent;
}
#course {
  width: 45vw !important;
  padding: 0;
  background: transparent;
}
#other {
  padding-right: 5vw;
  background: transparent;
}
.category-item {
  background: rgba(36, 36, 36, 0.95) !important;
  width: 15vw !important;
  height: 100% !important;
  box-shadow: 0px 0 15px #000000;
}
.course-item {
  background: rgba(36, 36, 36, 0.95) !important;
  height: 100% !important;
}
.other-item {
  background: rgba(46, 46, 46, 0.85);
  color: white !important;
  width: 20vw;
  height: 100%;
  box-shadow:0px 0 15px #000000;
  overflow: hidden;
}
</style>
