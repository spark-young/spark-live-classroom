<template>
  <el-container>
    <el-header id="nav">
      <MainHeader />
    </el-header>
    <el-container class="student-container">
      <el-aside class="student-info">
        <el-dialog :visible.sync="editUserInfoVisible" :show-close="false">
          <el-form
            :model="studentInfoDTO"
            :rules="studentInfoRules"
            ref="StudentInfoForm"
          >
            <el-form-item>
              <h2>教师信息修改</h2>
            </el-form-item>
            <el-form-item prop="email">
              <el-input
                class="el-dialog-input"
                placeholder="请输入您的邮箱"
                v-model="studentInfoDTO.email"
              ></el-input>
            </el-form-item>
            <el-form-item prop="mobile">
              <el-input
                placeholder="请输入您的电话"
                v-model="studentInfoDTO.mobile"
                maxlength="11"
                show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="info"
                @click="closeStudentInfo('StudentInfoForm')"
                >取消</el-button
              >
              <el-button
                type="primary"
                @click="submitStudentInfo('StudentInfoForm')"
                :loading="submitStudentInfoLoading"
                >{{
                  submitStudentInfoLoading ? "正在提交" : "提交修改"
                }}</el-button
              >
            </el-form-item>
          </el-form>
        </el-dialog>
        <div class="student-info-item">
          <div class="student-avatar">
            <p class="student-avatar-font">
              {{ studentInfo.studentName.substring(0, 1) }}
            </p>
          </div>
          <div class="student-name">{{ studentInfo.studentName }}</div>
          <div class="student-email">电子邮箱：{{ studentInfo.email }}</div>
          <div class="student-mobile">电话号码：{{ studentInfo.mobile }}</div>
          <el-button
            class="student-modifyInfobtn"
            type="primary"
            @click="editStudentInfo()"
            >修改个人信息</el-button
          >
        </div>
      </el-aside>
      <el-main class="student-main">
        <el-tabs tab-position="right" style="height: 77vh">
          <el-tab-pane label="我的课程">
            <CourseManager/>
          </el-tab-pane>
          <el-tab-pane label="我的日程">
            <Schedule/>
          </el-tab-pane>
          <el-tab-pane label="我的笔记">
            <Note/>
          </el-tab-pane>
          <el-tab-pane label="请求管理">
            <Request/>
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
import Schedule from "@/components/Schedule.vue";
import Note from "@/components/Note.vue";
import Request from "@/components/Request.vue";
import CourseManager from "@/components/course/courseManager.vue";
export default {
  name: "App",
  components: {
      MainHeader,
      Schedule,
      CourseManager,
      Note,
      Request
  },
  data() {
    return {
      studentInfo: {
        id: 0,
        studentName: "",
        email: "",
        mobile: "",
      },
      studentInfoDTO: {
        id: this.$store.state.id,
        email: "",
        mobile: "",
      },
      studentInfoRules: {
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
      submitStudentInfoLoading: false,
    };
  },
  mounted() {
    var self = this;
    self.getStudentInfo();
  },
  methods: {
    getStudentInfo() {
      var self = this;
      var url = self.$api.StudentInfo.url.split("=")[0];
      self.$api.StudentInfo.url = url + "=" + self.$store.state.id;
      self.$http.get(self.$api.StudentInfo).then((res) => {
        var sData = res.data.data;
        self.studentInfo.id = sData.id;
        self.studentInfo.studentName = sData.studentName;
        self.studentInfo.email = sData.email == "" ? "未填写" : sData.email;
        self.studentInfo.mobile =
          sData.mobile == null ? "未填写" : sData.mobile;
      });
    },
    editStudentInfo() {
      var self = this;
      self.studentInfoDTO.email = self.studentInfo.email;
      self.studentInfoDTO.mobile = self.studentInfo.mobile;
      self.editUserInfoVisible = true;
    },
    closeStudentInfo(StudentInfoForm) {
      var self = this;
      self.$refs[StudentInfoForm].resetFields();
      self.editUserInfoVisible = false;
    },
    submitStudentInfo(StudentInfoForm) {
      var self = this;

      self.submitStudentInfoLoading = true;
      self.$http
        .post(self.$api.UserInfoModify, self.studentInfoDTO)
        .then((res) => {
          if (res.data.success) {
            self.$message.success("修改成功！");
            self.studentInfo.email = self.studentInfoDTO.email;
            self.studentInfo.mobile = self.studentInfoDTO.mobile;
            self.submitStudentInfoLoading = false;
            self.closeStudentInfo(StudentInfoForm);
          } else if (
            res.data.statusCode == self.$resultCode.DATABASE_UPDATE_FAIL.code
          ) {
            self.$message.error(
              self.$resultCode.DATABASE_UPDATE_FAIL.message + "!"
            );
            self.submitStudentInfoLoading = false;
          }
        });
    },
  },
  watch: {
      
  }
};
</script>

<style>
.student-container {
  background: rgb(252, 252, 252);
}
.student-info {
  /* width:  */
  width: 25vw !important;
  height: 84vh !important;
}
.student-info-item {
  border-radius: 15px;
  margin-top: 1vh;
  margin-left: 1vw;
  height: 79vh;
  width: 21vw;
  background: rgb(238, 238, 238);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.student-main {
  width: 75vw !important;
  height: 84vh !important;
  /* background: black ; */
}

.student-avatar {
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
.student-avatar-font {
  font-size: 5em;
  color: white;
}
.student-name {
  padding-top: 28vh;
  font-size: 2em;
  font-weight: bold;
  color: black;
}
.student-email {
  margin-left: 2vw;
  margin-right: 2vw;
  padding-top: 6vh;
  font-size: 20px;
  word-break: break-all;
  color: black;
}
.student-mobile {
  padding-top: 1vh;
  font-size: 20px;
  color: black;
}
.student-modifyInfobtn {
  position: absolute;
  bottom: 16vh;
  left: 7vw;
  border-radius: 10px !important;
  border: none;
  background-color: rgb(128, 128, 128) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.student-modifyInfobtn:hover {
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset !important;
}
</style>