<template>
  <div v-loading="courseLoading">
    <el-menu
      v-loading="loading"
      class="manager-menu-style"
      text-color="rgb(30,30,30)"
    >
      <el-menu-item
        class="manager-menu-item-course"
        v-for="(item, i) in courseList"
        :key="i"
        :index="i + ''"
        @click="selectCourse(i)"
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
        <el-tooltip class="item" effect="dark" placement="top" content="取消订阅">
          <div class="course-menubtn-group">
            <el-button
              class="course-menubtn-item"
              type="info"
              icon="el-icon-delete"
              @click="cancleCourse()"
              circle
            ></el-button>
          </div>
        </el-tooltip>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      courseList: [],
      courseList_photo_color: [],
      sid: -1,

      curIndex: -1,
      curCourseId: -1,

      loading: true,
      courseLoading: true,
    };
  },
  mounted() {
    var self = this;
    self.courseList_photo_color = self.$photoColor.colorList;
    self.sid = self.$store.state.id;
    self.changeCourse();
    self.loading = false;
    self.courseLoading = false;
  },
  methods: {
    changeCourse() {
      var self = this;
      self.typeId = window.location.href.split("=")[1];
      var url = self.$api.CourseListBySid.url;
      self.$api.CourseListBySid.url = url.split("=")[0] + "=" + self.sid;
      self.$http.get(self.$api.CourseListBySid).then((res) => {
        var courseData = JSON.parse(res.data.data);
        self.courseList = courseData;
        console.log(self.courseList);
      });
    },
    selectCourse(i) {
      var self = this;
      self.curIndex = i;
      self.curCourseId = self.courseList[i].id;
    },
    cancleCourse() {
        var self = this;
      self
        .$prompt("此操作将取消订阅该课程, 请说明退课原因?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /\S/,
          inputErrorMessage: '原因不能为空'
        }).then(({value}) => {
          let unsubcribeCourseDTO = {
            cid: self.curCourseId,
            sid: self.$store.state.id,
            content: value
          }
          console.log(value);
          self.$http.post(self.$api.CourseUnsubscribe,unsubcribeCourseDTO).then((res) => {
            if (res.data.success) {
              self.$message.success("已向教师发送取消订阅该课程的请求!");
            } else if(res.data.statusCode == self.$resultCode.REQUEST_HAS_EXISTED.code){
              self.$message.warning("您发送的取消订阅课程的请求还未被处理!");
            }else {
              self.$message.error("未知错误!");
            }
          });
        })
        .catch(() => {
          self.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    }
  },
};
</script>

<style>
.manager-menu-style {
  background: transparent;
  color: black;
  width: 54vw !important;
  background: transparent !important;
  border: none !important;
}
.manager-menu-item-course {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 12vh !important;
  width: 60vw;
  line-height: 6vh !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.manager-menu-add-course {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 12vh !important;
  width: 60vw !important;
  line-height: 6vh !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
/* 鼠标覆盖的样式 */
.manager-menu-item-course:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: black !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}

/* 被选中的样式 */
.manager-menu-item-course.is-active {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.75) !important;
  font-weight: bold;
  color: white !important;
}

.photo {
  height: 4em;
  width: 4em;
  border-radius: 50%;
  background: red;
  display: flex;
  justify-content: center;
  align-items: center;
}
.photo-font {
  padding-top: 0 !important;
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
.course-menubtn-group {
  position: absolute;
  right: 4vw;
  top: 4vh;
  display: flex;
}
.course-menubtn-item {
  width: 3em;
  height: 3em;
  color: white !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
</style>