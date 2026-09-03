<template>
  <div v-loading="comLoading">
    <el-menu
      v-if="showCourseList"
      v-loading="loading"
      class="el-menu-style"
      text-color="rgb(216, 216, 216)"
    >
      <el-menu-item
        class="el-menu-item-course"
        v-for="(item, i) in courseList"
        :key="i"
        :index="i + ''"
        @click="toCourseDetail(item.id)"
      >
        <div>
          <div class="photo" :class="courseList_photo_color[i % 7]">
            <p class="photo-font">{{ courseList[i].name.substring(0, 1) }}</p>
          </div>
        </div>
        <div>
          <span class="course-name" slot="title">{{ courseList[i].name }}</span>
          <span class="course-typeName" slot="title">{{
            courseList[i].typeName
          }}</span>
          <span class="course-description" slot="title">{{
            courseList[i].description
          }}</span>
        </div>
      </el-menu-item>
    </el-menu>
    <div class="course-detail" v-if="showCourseDetail">
      <div class="course-detail-header">
        <span class="back-item" @click="backToBefore()">⬅返 回</span>
        <h2 class="detail-title">{{ courseDetails.name }}</h2>
        <el-button class="subscribe-button" type="success" @click="subcribeIt()"
          >订阅</el-button
        >
      </div>
      <div class="course-detail-content">
        <div
          class="detail-photo"
          :class="courseList_photo_color[Math.round(Math.random() * 7)]"
        >
          <p class="detail-photo-font">
            {{ courseDetails.name.substring(0, 1) }}
          </p>
        </div>
        <pre class="detail-description">{{ courseDetails.description }}</pre>
      </div>
      <div class="course-detail-part">
        <h3>课程章节</h3>
        <h3>by {{ courseDetails.teacherName }}</h3>
      </div>
      <el-tree
        class="detail-chapter"
        :data="courseDetails.chapter"
        :props="defaultProps"
      ></el-tree>
    </div>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      courseList: [],
      courseList_photo_color: [],
      showCourseList: true,
      showCourseDetail: false,
      typeId: -1,
      loading: true,
      courseId: -1,
      curIndex: -1,
      comLoading: true,
      courseDetails: {
        teacherName: "",
        name: "",
        description: "",
        url: "",
        chapter: [],
      },
      defaultProps: {
        children: "children",
        label: "title",
      },
    };
  },
  mounted() {
    var self = this;
    // let typeId = window.location.href.split("=")[1];
    // self.$api.CourseListByTypeId.url += self.typeId;
    self.courseList_photo_color = self.$photoColor.colorList;
    self.typeId = window.location.href.split("=")[1];
    self.changeCourse();
    self.loading = false;
    self.comLoading = false;
  },
  methods: {
    changeCourse() {
      var self = this;
      if (window.location.href.split("#")[1] == "/") {
        self.$http.get(self.$api.CourseList).then((res) => {
          var courseData = JSON.parse(res.data.data);
          self.courseList = courseData;
          console.log(self.courseList);
        });
      } else if (window.location.href.split("?")[1].split("=")[0] == "typeId") {
        self.typeId = window.location.href.split("=")[1];
        var url = self.$api.CourseListByTypeId.url;
        url = url.split("=")[0] + "=" + self.typeId;
        self.$api.CourseListByTypeId.url = url;
        self.$http.get(self.$api.CourseListByTypeId).then((res) => {
          var courseData = JSON.parse(res.data.data);
          self.courseList = courseData;
          console.log(self.courseList);
        });
      } else if (window.location.href.split("?")[1].split("=")[0] == "content") {
        var content = window.location.href.split("=")[1];
        var url = self.$api.CourseListBySearch.url;
        self.$api.CourseListBySearch.url = url.split("=")[0] + "=" + content;
        self.$http.get(self.$api.CourseListBySearch).then((res) => {
          var courseData = JSON.parse(res.data.data);
          self.courseList = courseData;
          console.log(self.courseList);
        });
      } else {
      }
    },
    toCourseDetail(i) {
      var self = this;
      self.courseId = i;
      console.log("preHref" + self.preHref);
      if (window.location.href.split("?")[1] == "courseId=" + self.courseId) {
        self.showCourseList = false;
        self.toDetail();
        return;
      }
      self.$router.push("/course/details?courseId=" + self.courseId);
    },
    toList() {
      var self = this;
      self.comLoading = true;
      self.showCourseDetail = false;
      self.showCourseList = true;
      self.comLoading = false;
    },
    toDetail() {
      var self = this;
      self.comLoading = true;
      self.showCourseList = false;
      setTimeout(() => {
        self.showCourseDetail = true;
      },500);
      
      self.comLoading = false;

      var url = self.$api.CourseDetails.url;
      url = url.split("=")[0] + "=" + self.courseId;
      self.$api.CourseDetails.url = url;

      console.log(self.$api.CourseDetails.url);
      self.$http.get(self.$api.CourseDetails).then((res) => {
        var details = JSON.parse(res.data.data);
        self.courseDetails.teacherName = details.teacherName;
        self.courseDetails.name = details.courseName;
        self.courseDetails.description = details.courseDescription;
        self.courseDetails.url = details.url;
        if (typeof details.chapterJSON != "undefined") {
          self.courseDetails.chapter = JSON.parse(details.chapterJSON);
        }
      });
    },
    subcribeIt() {
      var self = this;
      if (self.$store.state.id == 0) {
        this.$confirm("您还未登录，是否先去登录？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            self.$router.push({ name: "Login" });
          })
          .catch(() => {});
      } else if (self.$store.state.roleId == 1) {
        this.$message.warning("教师不能点击订阅哦！");
      } else {
        var url = self.$api.SubscribeCourse.url;
        url =
          url.split("=")[0] +
          "=" +
          self.courseId +
          "&sid=" +
          self.$store.state.id;
        self.$api.SubscribeCourse.url = url;
        self.$http.get(self.$api.SubscribeCourse).then((res) => {
          if (res.data.success) {
            this.$message.success(res.data.message);
          } else if (res.data.statusCode == self.$resultCode.COMMON_FAIL.code) {
            this.$message.error(res.data.message);
          } else {
            self.$message.error("订阅，未知错误！");
          }
        });
      }
    },
    backToBefore() {
      var self = this;
      self.courseDetails.chapter = [];
      self.$router.go(-1);
    },
  },
  watch: {
    $route() {
      var self = this;
      if (window.location.href.split("#")[1] == "/") {
        self.typeId = "undefined";
        self.toList();
        self.loading = true;
        self.changeCourse();
        setTimeout(() => {
          self.loading = false;
        }, 500);
      } else if (window.location.href.split("?")[1].split("=")[0] == "typeId") {
        self.toList();
        self.loading = true;
        self.changeCourse();
        setTimeout(() => {
          self.loading = false;
        }, 500);
      } else if (
        window.location.href.split("?")[1].split("=")[0] == "content"
      ) {
        self.toList();
        self.loading = true;
        self.changeCourse();
        setTimeout(() => {
          self.loading = false;
        }, 500);
      } else if (
        window.location.href.split("?")[1].split("=")[0] == "courseId"
      ) {
        self.toDetail();
      }

      // self.reFresh = true;
    },
  },
};
</script>

<style>
.el-menu-style {
  background: transparent;
  color: white;
  background: transparent !important;
  border: none !important;
}
.el-menu-item-course {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: transparent;
  height: 12vh !important;
  line-height: 6vh !important;
  color: white;
}
/* 鼠标覆盖的样式 */
.el-menu-item-course:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: white !important;
  box-shadow: 0px 0 10px #ffffff;
}
/* 被选中的样式 */
.el-menu-item-course.is-active {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: white !important;
}

.photo {
  margin-top: 15%;
  height: 4em;
  width: 4em;
  border-radius: 50%;
  background: red;
}
.photo-font {
  padding-top: 15%;
  font-size: 3em;
  color: white !important;
}
.course-name {
  position: absolute;
  left: 6em;
  font-weight: bold;
  height: 4vh;
}
.course-typeName {
  position: absolute;
  right: 1vw;
  font-weight: bold;
  height: 4vh;
}
.course-description {
  position: absolute;
  text-align: left;
  top: 4vh;
  left: 6em;
  height: 8vh;
  line-height: 4vh;
  width: 30vw;
  white-space: pre-wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/* 以下是courseDetails */
.course-detail {
  display: flex;
  flex-direction: column;
}
.course-detail-header {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.back-item {
  margin-top: 3vh;
  cursor: pointer;
  color: white;
}
.back-item:hover {
  cursor: pointer;
  color: red;
}
.detail-title {
  padding-top: 2vh;
  color: white;
}
.subscribe-button {
  border: none !important;
  width: 80px;
  height: 40px;
  margin-top: 2vh !important;
  background: rgb(139, 0, 0) !important;
}
.course-detail-content {
  display: flex;
  flex-direction: row;
  justify-content: left;
  margin-top: 2vh;
  margin-left: 2vw;
}
.detail-description {
  margin-left: 2vw;
  font-size: 18px;
  color: white;
  text-align: left;
  width: 30vw;
  white-space: pre-wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.detail-photo {
  height: 6em;
  width: 6em;
  border-radius: 50%;
  background: transparent;
  background: #ff6666;
  display: flex;
  justify-content: center;
  align-items: center;
}
.detail-photo-font {
  font-size: 5em;
  color: white !important;
}

.course-detail-part {
  margin-top: 2vh;
  display: flex;
  justify-content: space-between;
  color: white;
}

.detail-chapter {
  margin-left: 1vh;
  margin-right: 1vh;
  background: transparent !important;
  color: white !important;
}
.el-tree-node__content {
  background: transparent !important;
}
.el-tree-node__content:hover {
  background-color: rgba(30, 30, 30, 0.75) !important;
}
</style>