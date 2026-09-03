<template>
  <div>
    <el-calendar class="date-content" v-model="value" v-show="calendarVisiable">
      <template #dateCell="{ date, data }">
        <div @click="scheduleDetails(data)" class="date-cell">
          <div class="calendar-day">
            {{ data.day.split("-").slice(2).join("-") }}
          </div>
          <div v-for="item in calendarData">
            <div v-if="item.years === data.day.split('-').slice(0)[0]">
              <div v-if="item.months === data.day.split('-').slice(1)[0]">
                <div
                  v-if="
                    item.days.indexOf(data.day.split('-').slice(2).join('-')) !=
                    -1
                  "
                >
                  <!-- <el-tooltip
                  class="item"
                  effect="dark"
                  :content="item.things"
                  placement="right"
                > -->
                  <div class="day-has-lesson">当天{{ item.count }}节课</div>
                  <!-- </el-tooltip> -->
                </div>
                <div v-else></div>
              </div>
              <div v-else></div>
            </div>
            <div v-else></div>
          </div>
        </div>
      </template>
    </el-calendar>
    <el-menu
      class="schedule-day"
      text-color="rgb(30,30,30)"
      v-show="scheduleVisiable"
    >
      <el-menu-item class="schedule-day-header">
        <el-button
          class="schedule-day-back"
          type="info"
          icon="el-icon-back"
          @click="backToCalendar()"
          circle
        ></el-button>
        <h2 style="position: absolute; left: 0; right: 0; z-index: 1">
          {{ curDays }}
        </h2>
      </el-menu-item>
      <el-menu-item
        class="schedule-menu-item"
        v-for="(item, i) in scheduleList"
        :key="i"
        :index="i + ''"
      >
        <div>{{ item.typeName }}一一</div>
        <div>{{ item.courseName }}一</div>
        <div>{{ item.chapterName }}</div>
        <div class="lesson-time">
          开课时间：<el-date-picker
            v-model="item.classTime"
            type="datetime"
            placeholder="选择日期时间"
          >
          </el-date-picker>
        </div>
        <div style="position: absolute; right: 1vw; bottom: 1vh">
          <el-button type="primary" @click="enterRoom(item)"
            >进入直播间</el-button
          >
          <el-button type="info" @click="changeTime(i)">提交修改</el-button>
        </div>
        <div
          style="
            position: absolute;
            bottom: 1vh;
            display: flex;
            font-weight: bold;
          "
        >
          <div>{{ item.tName }}←→</div>
          <div>{{ item.sName }}</div>
        </div>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      calendarData: [],
      scheduleList: [],
      value: new Date(),

      calendarVisiable: true,
      scheduleVisiable: false,

      curDays: "",
      curLessonIndex: -1,
    };
  },
  mounted() {
    var self = this;

    if (self.$store.state.roleId == 1) {
      var url = self.$api.CalendarAllTeacher.url;
      self.$api.CalendarAllTeacher.url =
        url.split("=")[0] + "=" + self.$store.state.id;
      self.$http.get(self.$api.CalendarAllTeacher).then((res) => {
        self.calendarData = res.data.data;
        console.log(res.data.data);
      });
    } else {
      var url = self.$api.CalendarAllStudent.url;
      self.$api.CalendarAllStudent.url =
        url.split("=")[0] + "=" + self.$store.state.id;
      self.$http.get(self.$api.CalendarAllStudent).then((res) => {
        self.calendarData = res.data.data;
        console.log(res.data.data);
      });
    }
  },
  methods: {
    scheduleDetails(data) {
      var self = this;
      self.scheduleList = [];
      self.curDays =
        data.day.split("-")[0] +
        "年" +
        data.day.split("-")[1] +
        "月" +
        data.day.split("-")[2] +
        "日";

      let scheduleDayDTO = {
        id: self.$store.state.id,
        day: data.day.split("T")[0] + "T00:00:00",
      };
      console.log(scheduleDayDTO);
      if (self.$store.state.roleId == 1) {
        self.$http
          .post(self.$api.ScheduleDayTeacher, scheduleDayDTO)
          .then((res) => {
            console.log(res.data.data);
            self.scheduleList = JSON.parse(res.data.data);
          });
      } else {
        self.$http
          .post(self.$api.ScheduleDayStudent, scheduleDayDTO)
          .then((res) => {
            console.log(res.data.data);
            self.scheduleList = JSON.parse(res.data.data);
          });
      }

      self.calendarVisiable = false;
      self.scheduleVisiable = true;
    },
    backToCalendar() {
      var self = this;
      self.calendarVisiable = true;
      self.scheduleVisiable = false;
    },
    enterRoom(item) {
      var self = this;
      if (self.$store.state.roleId == 1) {
        self.$router.push("/room/teacher?ijt=" + item.roomId);
      } else {
        self.$router.push("/room/student?ijt=" + item.roomId);
      }
    },
    changeTime(i) {
      var self = this;
      self.curLessonIndex = i;

      self
        .$prompt("您将请求修改该课程的开课时间, 请说明原因?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /\S/,
          inputErrorMessage: "原因不能为空",
        })
        .then(({ value }) => {
          let LessonInfoDTO = {
            cid: self.scheduleList[i].cid,
            chid: self.scheduleList[i].chid,
            tid: self.scheduleList[i].tid,
            sid: self.scheduleList[i].sid,
            teacher: self.$store.state.roleId == 1 ? true : false,
            content: value,
            classTime: self.scheduleList[i].classTime,
          };
          console.log(value);
          self.$http.post(self.$api.ScheduleLessonSet,LessonInfoDTO).then((res) => {
            if (res.data.success) {
              self.$message.success("已提交请求，请等待处理!");
            } else {
              self.$message.error(res.data.message);
            }
          });
        })
        .catch(() => {
          self.$message({
            type: "info",
            message: "已取消处理",
          });
        });
    },
  },
};
</script>

<style>
.date-content {
  height: 80vh;
}
.el-calendar-day {
  margin: 0;
  padding: 4px !important;
  background: white;
  height: 10vh !important;
}
.date-cell {
  border-radius: 5px;
  background: rgb(238, 238, 238);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
  width: 100%;
  height: 100%;
}
.schedule-day {
  background: transparent;
  color: black;
  width: 64vw !important;
  background: transparent !important;
  border: none !important;
}
.schedule-day-header {
  display: flex;
  align-items: center;
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
.schedule-day-header:hover,
.schedule-menu-item:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: black !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}
.schedule-day-header.is-active {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.75) !important;
  font-weight: bold;
  color: white !important;
}
.schedule-day-back {
  width: 3em;
  height: 3em;
  color: white !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
  z-index: 2;
}
.schedule-menu-item {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 100px !important;
  width: 60vw;
  line-height: 20px !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.lesson-time {
  position: absolute;
  top: 2px;
  right: 1vw;
}
</style>