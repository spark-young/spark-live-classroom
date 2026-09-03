<template>
  <div class="request-div">
    <h1 v-if="requestList.length == 0">暂无请求</h1>
    <el-menu class="request-menu" text-color="rgb(30,30,30)">
      <el-menu-item
        class="request-menu-item"
        v-for="(item, i) in requestList"
        :key="i"
        :index="i + ''"
      >
        <div>
          <span class="request-menu-item-name">{{
            requestList[i].requestName
          }}</span>
          <h2 class="request-menu-item-header">
            {{ requestList[i].requestType }}
          </h2>
          <div>
            <span class="request-menu-item-content">
              {{ requestList[i].requestContent }}</span
            >
          </div>
          <div class="request-menu-item-reason">
            原因：{{ requestList[i].requestReason }}
          </div>
          <div class="request-menu-item-btl">
            <el-button
              class="request-btl-style"
              type="success"
              @click="accept(i)"
              >{{
                requestList[i].requestType != "知会" ? "通过" : "确认"
              }}</el-button
            >
            <el-button
              class="request-btl-style"
              type="warning"
              @click="reject(i)"
              v-if="requestList[i].requestType != '知会'"
              >驳回</el-button
            >
          </div>
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
      requestList: [],
      tid: -1,
      count: -1,
      sid: -1,
    };
  },
  mounted() {
    var self = this;
    if (self.$store.state.roleId == 1) {
      self.getTeacherList();
      self.getTeacherCount();
    } else {
      self.getStudentList();
      self.getStudentCount();
    }
  },
  methods: {
    async getTeacherCount(){
      var self = this;
      var url = self.$api.RequestCountByTid.url;
      self.$api.RequestCountByTid.url = url.split("=")[0] + "=" + self.tid;
      window.timer = setInterval(() => {
        setTimeout(() => {
          self.$http.get(self.$api.RequestCountByTid).then((res) => {
            if(self.count == -1){
              self.count = res.data.data;
            } else if(self.count != res.data.data){
              self.count = res.data.data;
              self.$notify.info({
                  title: '新请求',
                  message: '您有一条新请求需要处理！'
                });
                self.getTeacherList();
            }
          });
      },0)},3000);//每3s查一次请求数量
    },
    async getStudentCount(){
      var self = this;
      var url = self.$api.RequestCountBySid.url;
      self.$api.RequestCountBySid.url = url.split("=")[0] + "=" + self.sid;
      window.timer = setInterval(() => {
        setTimeout(() => {
          self.$http.get(self.$api.RequestCountBySid).then((res) => {
            if(self.count == -1){
              self.count = res.data.data;
            } else if(self.count != res.data.data){
              self.count = res.data.data;
              self.$notify.info({
                  title: '新请求',
                  message: '您有一条新请求需要处理！'
                });
              self.getStudentList();
            }
          });
      },0)},3000);//每3s查一次请求数量
    },
    getTeacherList() {
      var self = this;
      var url = self.$api.RequestListByTid.url;
      self.tid = self.$store.state.id;
      self.$api.RequestListByTid.url = url.split("=")[0] + "=" + self.tid;
      self.$http.get(self.$api.RequestListByTid).then((res) => {
        if (res.data.success) {
          self.requestList = JSON.parse(res.data.data);
          console.log(self.requestList);
        }
      });
    },
    getStudentList() {
      var self = this;
      var url = self.$api.RequestListBySid.url;
      self.sid = self.$store.state.id;
      self.$api.RequestListBySid.url = url.split("=")[0] + "=" + self.sid;
      self.$http.get(self.$api.RequestListBySid).then((res) => {
        if (res.data.success) {
          self.requestList = JSON.parse(res.data.data);
          console.log(res.data.data + "!!!!!");
        }
      });
    },
    accept(i){
      var self = this;
      
        let acceptDTO = {
          id: self.requestList[i].id,
          accept: true,
          content: ""
        }
        self.$http.post(self.$api.RequestSolve,acceptDTO).then((res) => {
          if(res.data.success){
            self.count--;
            if(self.requestList[i].requestType == "知会"){
              self.$message.success("已知会！");
            } else if(self.requestList[i].requestType == "取消订阅课程"){
              self.$message.success("已通过取消课程的请求，并知会！");
            } else if(self.requestList[i].requestType == "变更开课时间"){
              self.$message.success("已通过变更开课时间的请求，并知会！");
            }
              self.requestList.splice(i,1);
          }
        });
    },
    reject(i) {
      var self = this;
      self.$prompt("您将驳回该请求, 请说明原因?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /\S/,
          inputErrorMessage: '原因不能为空'
        }).then(({value}) => {
          let rejectDTO = {
            id: self.requestList[i].id,
            accept: false,
            content: value
          }
          console.log(value);
          self.$http.post(self.$api.RequestSolve,rejectDTO).then((res) => {
            if (res.data.success) {
              self.$message.success("已驳回该请求!");
              self.count--;
              self.requestList.splice(i,1);
            }else {
              self.$message.error("未知错误!");
            }
          });
        })
        .catch(() => {
          self.$message({
            type: "info",
            message: "已取消处理",
          });
        });
    }
  },
};
</script>

<style>
.request-div {
  width: 62vw;
  height: 80vh;
  overflow-y: scroll;
}
.request-div::-webkit-scrollbar {
  width: 0;
}
.request-menu {
  background: transparent;
  color: black;
  width: 64vw !important;
  background: transparent !important;
  border: none !important;
}
.request-menu-item {
  padding: 0 !important;
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238) !important;
  height: 160px !important;
  width: 60vw;
  line-height: 48px !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.request-menu-item:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: black !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}
.request-menu-item-name {
  border-radius: 5px;
  position: absolute;
  top: 0px;
  left: 1vw;
  height: 28px;
}
.request-menu-item-header {
  position: absolute;
  top: -2px;
  left: 50%;
}
.request-menu-item-content {
  border-radius: 5px;
  position: absolute;
  font-weight: bold;
  top: 18px;
  left: 1vw;
  height: 28px;
}
.request-menu-item-reason {
  position: absolute;
  top: 52px;
  left: 1vw;
  padding: 10px;
  line-height: 24px;
  text-align: left;
  width: 56vw;
  height: 80px;
  border-radius: 5px;
  background: rgb(218, 218, 218);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
  white-space: pre-wrap;
  overflow-x: hidden;
  overflow-y: scroll;
  text-overflow: ellipsis;
}
.request-menu-item-btl {
  position: absolute;
  top: 2px;
  height: 80px;
  right: 1vw;
}
.request-btl-style {
  padding: 12px 4px 12px 4px !important;
  border-radius: 25px !important;
  width: 60px;
}
</style>