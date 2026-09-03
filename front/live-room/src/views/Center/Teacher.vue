<template>
  <el-container>
    <el-header id="nav">
      <MainHeader />
    </el-header>
    <el-container class="teacher-container">
      <el-aside class="teacher-info">
        <el-dialog :visible.sync="editUserInfoVisible" :show-close="false">
          <el-form
            :model="teacherInfoDTO"
            :rules="teacherInfoRules"
            ref="TeacherInfoForm"
          >
            <el-form-item>
              <h2>教师信息修改</h2>
            </el-form-item>
            <el-form-item prop="email">
              <el-input
                class="el-dialog-input"
                placeholder="请输入您的邮箱"
                v-model="teacherInfoDTO.email"
              ></el-input>
            </el-form-item>
            <el-form-item prop="mobile">
              <el-input
                placeholder="请输入您的电话"
                v-model="teacherInfoDTO.mobile"
                maxlength="11"
                show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="info"
                @click="closeTeacherInfo('TeacherInfoForm')"
                >取消</el-button
              >
              <el-button
                type="primary"
                @click="submitTeacherInfo('TeacherInfoForm')"
                :loading="submitTeacherInfoLoading"
                >{{
                  submitTeacherInfoLoading ? "正在提交" : "提交修改"
                }}</el-button
              >
            </el-form-item>
          </el-form>
        </el-dialog>
        <div class="teacher-info-item">
          <div class="teacher-avatar">
            <p class="teacher-avatar-font">
              {{ teacherInfo.teacherName.substring(0, 1) }}
            </p>
          </div>
          <div class="teacher-name">{{ teacherInfo.teacherName }}</div>
          <div class="teacher-email">电子邮箱：{{ teacherInfo.email }}</div>
          <div class="teacher-mobile">电话号码：{{ teacherInfo.mobile }}</div>
          <el-button
            class="teacher-modifyInfobtn"
            type="primary"
            @click="editTeacherInfo()"
            >修改个人信息</el-button
          >
        </div>
      </el-aside>
      <el-main class="teacher-main">
        <el-tabs tab-position="right" style="height: 77vh">
          <el-tab-pane label="课程管理">
            <CourseManager />
          </el-tab-pane>
          <el-tab-pane label="我的日程">
            <Schedule />
          </el-tab-pane>
          <el-tab-pane label="请求管理">
            <Request />
          </el-tab-pane>
          <!-- <el-tab-pane label="创建课程">创建课程</el-tab-pane> -->
          <!-- <el-tab-pane label="定时任务补偿">定时任务补偿</el-tab-pane> -->
        </el-tabs>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import MainHeader from "@/components/Header/MainHeader.vue";
import CourseManager from "@/components/course/courseManager.vue";
import Schedule from "@/components/Schedule.vue";
import Request from "@/components/Request.vue";
export default {
  name: "App",
  components: {
    MainHeader,
    CourseManager,
    Schedule,
    Request,
  },
  data() {
    return {
      teacherInfo: {
        id: 0,
        teacherName: "",
        email: "",
        mobile: "",
      },
      teacherInfoDTO: {
        id: this.$store.state.id,
        email: "",
        mobile: "",
      },
      teacherInfoRules: {
        email: [
          { required: true, message: "请输入您的邮箱" },
          {
            pattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
            message: "请输入正确的邮箱",
          },
        ],
        mobile: [
          { required: true, message: "请输入您的电话" },
          { pattern: /[1][0-9]{10}$/, message: "请输入正确的电话号码" },
        ],
      },
      editUserInfoVisible: false,
      submitTeacherInfoLoading: false,
    };
  },
  mounted() {
    var self = this;
    self.getTeacherInfo();
  },
  methods: {
    getTeacherInfo() {
      var self = this;
      var url = self.$api.TeacherInfo.url.split("=")[0];
      self.$api.TeacherInfo.url = url + "=" + self.$store.state.id;
      self.$http.get(self.$api.TeacherInfo).then((res) => {
        var tiData = res.data.data;
        self.teacherInfo.id = tiData.id;
        self.teacherInfo.teacherName = tiData.teacherName;
        self.teacherInfo.email = tiData.email == "" ? "未填写" : tiData.email;
        self.teacherInfo.mobile =
          tiData.mobile == null ? "未填写" : tiData.mobile;
      });
    },
    editTeacherInfo() {
      var self = this;
      self.teacherInfoDTO.email =
        self.teacherInfo.email === "未填写" ? "" : self.teacherInfo.email;
      self.teacherInfoDTO.mobile =
        self.teacherInfo.mobile === "未填写" ? "" : self.teacherInfo.mobile;
      self.editUserInfoVisible = true;
    },
    closeTeacherInfo(TeacherInfoForm) {
      var self = this;
      self.$refs[TeacherInfoForm].resetFields();
      self.editUserInfoVisible = false;
    },
    submitTeacherInfo(TeacherInfoForm) {
      var self = this;

      self.$refs[TeacherInfoForm].validate((valid) => {
        if (valid) {
          self.submitTeacherInfoLoading = true;
          self.$http
            .post(self.$api.UserInfoModify, self.teacherInfoDTO)
            .then((res) => {
              if (res.data.success) {
                self.$message.success("修改成功！");
                self.teacherInfo.email =
                  self.teacherInfoDTO.email === ""
                    ? "未填写"
                    : self.teacherInfoDTO.email;
                self.teacherInfo.mobile =
                  self.teacherInfoDTO.mobile === ""
                    ? "未填写"
                    : self.teacherInfoDTO.mobile;
                self.submitTeacherInfoLoading = false;
                self.closeTeacherInfo(TeacherInfoForm);
              } else if (
                res.data.statusCode ==
                self.$resultCode.DATABASE_UPDATE_FAIL.code
              ) {
                self.$message.error(
                  self.$resultCode.DATABASE_UPDATE_FAIL.message + "!"
                );
                self.submitTeacherInfoLoading = false;
              }
            });
        } else {
          self.$message.warning("请正确输入要求的信息！");
        }
      });
    },
  },
};
</script>

<style>
.teacher-container {
  background: rgb(252, 252, 252);
}
.teacher-info {
  /* width:  */
  width: 25vw !important;
  height: 84vh !important;
}
.teacher-info-item {
  border-radius: 15px;
  margin-top: 1vh;
  margin-left: 1vw;
  height: 79vh;
  width: 21vw;
  background: rgb(238, 238, 238);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.teacher-main {
  width: 75vw !important;
  height: 84vh !important;
  /* background: black ; */
}

.teacher-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 14vh;
  left: 7vw;
  width: 8em;
  height: 8em;
  border-radius: 50%;
  background: gray;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}
.teacher-avatar-font {
  font-size: 5em;
  color: white;
}
.teacher-name {
  padding-top: 28vh;
  font-size: 2em;
  font-weight: bold;
  color: black;
}
.teacher-email {
  margin-left: 2vw;
  margin-right: 2vw;
  padding-top: 6vh;
  font-size: 20px;
  word-break: break-all;
  color: black;
}
.teacher-mobile {
  padding-top: 1vh;
  font-size: 20px;
  color: black;
}
.teacher-modifyInfobtn {
  position: absolute;
  bottom: 16vh;
  left: 7vw;
  border-radius: 10px !important;
  border: none;
  background-color: rgb(128, 128, 128) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.teacher-modifyInfobtn:hover {
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset !important;
}
</style>