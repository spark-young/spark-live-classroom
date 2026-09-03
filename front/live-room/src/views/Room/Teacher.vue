<template>
  <el-container
    style="margin: 0; padding: 0; width: 100%; height: 100%; display: flex"
  >
    <el-header id="nav">
      <TapBar />
    </el-header>
    <el-container>
      <el-aside style="width: 12vw; background-color: rgb(46, 50, 61)">
        <div style="text-align: center">
          <div
            id="currentSources"
            v-html="currentSources_text"
            style="font-size: 20px; color: red"
          ></div>
          <br />
          <div
            id="remoteAnswser"
            v-html="remoteAnswser_text"
            style="font-size: 20px; color: red"
          ></div>
        </div>
        <el-menu>
          <el-menu-item
            class="el-menu-item-left"
            index="1"
            @click="getMedia"
          >
            <i></i>
            <span slot="title">开启媒体</span>
          </el-menu-item>
          <el-menu-item class="el-menu-item-left" index="2" @click="openCamera">
            <!-- <i></i> -->
            <span slot="title">呼叫对方</span>
          </el-menu-item>
          <!-- <el-menu-item class="el-menu-item-left" index="3" @click="startLive">
            <i></i>
            <span slot="title">换直播源</span>
          </el-menu-item> -->
          <el-menu-item class="el-menu-item-left" index="3" @click="fullScreen">
            <i></i>
            <span slot="title">对方全屏</span>
          </el-menu-item>
          <el-menu-item class="el-menu-item-left" index="4" @click="hangup">
            <i></i>
            <span slot="title">结束挂断</span>
          </el-menu-item>
          <el-menu-item
            class="el-menu-item-left"
            index="7"
            @click="reloadRemote"
          >
            <i></i>
            <span slot="title">远程刷新</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="el-main-style">
        <div style="width: 40%; justify-content: center">
          <div
            id="p"
            title="本地摄像头"
            style="width: 98%; height: 50%; margin-left: 1%"
          >
            <p line-height="10%">本地相机</p>
            <video
              class="video-item"
              id="vi"
              ref="local_video"
              autoplay="autoplay"
              loop="loop"
              muted="false"
              poster="../../images/left1.jpg"
              controlsList="nodownload"
              style="width: 100%; height: 95%"
            ></video>
          </div>
          <div
            id="p1"
            title="屏幕共享"
            style="width: 98%; height: 50%; margin-left: 1%"
          >
            <p line-height="10%">屏幕共享</p>
            <video
              class="video-item"
              id="screen_div"
              ref="screen_video"
              autoplay="autoplay"
              loop="loop"
              muted="false"
              poster="../../images/left2.jpg"
              controlsList="nodownload"
              style="width: 100%; height: 95%"
            ></video>
          </div>
        </div>
        <div
          id="p2"
          title="对方媒体"
          style="width: 100%; height: 100%; padding: 1px"
        >
          <p style="height=10%">对方媒体</p>
          <video
            class="video-item"
            id="remoteAudio"
            ref="remoteAudio_video"
            autoplay="autoplay"
            muted="false"
            poster="../../images/main.jpg"
            controls="controls"
            style="width: 98%; height: 90%"
          ></video>
        </div>
      </el-main>
      <el-aside
        width="20%"
        overflow="scroll"
        style="margin: 0; padding: 0; background-color: rgb(238, 241, 246)"
      >
        <div style="width: 100%; height: 76vh; position: relative; top: 0px">
          <div
            id="message-status"
            style="
              margin-top: 0px;
              font-size: 10pt;
              border: 0px solid #cad9ea;
              width: 100%;
              height: 10%;
              text-align: center;
            "
            v-html="message_status_text"
          ></div>
          <div
            id="message-content"
            data-options="multiline:true"
            style="
              line-height: 6vh;
              width: 100%;
              height: 90%;
              justify-content: center;
              overflow: scroll;
              text-align: left;
            "
            v-html="message_content_text"
          ></div>
        </div>
        <el-input
          id="text-input"
          v-model="message_input"
          placeholder="请输入内容"
          @keyup.enter.native="WebSocketSend"
          style="
            width: 96%;
            font-size: 18px;
            background-color: rgb(238, 241, 246);
          "
        />
      </el-aside>
    </el-container>
  </el-container>
</template>
../../assets/js/common.js
<script>
import common from "@/assets/js/common.js";
import TapBar from "@/components/Header/TapBar.vue";
import "@/assets/js/adapter-latest.js";
let localMediaStream = null;
let localCameraStream = null;
console.log(localMediaStream + localCameraStream);
export default {
  components: {
    TapBar,
  },
  data() {
    return {
      message_input: "",
      message_content_text: "",
      message_status_text: "加载中...",
      lockReconnect: false, //避免重复连接
      wsUrl: "wss://spark-young.top/e3/",
      webSocket: new WebSocket("wss://spark-young.top/e3/"),
      val: "",
      ijt: "",
      currentSources_text: "",
      remoteAnswser_text: "",
      // acctiveStream: 10,
      offerer: null,
      offerer_camera: null,
      offerer_screen: null,
      localVideoObj: null,
      //心跳检测
      heartCheck: {
        timeout: 3000, //每隔三秒发送心跳
        num: 3, //3次心跳均未响应重连
        timeoutObj: null,
        serverTimeoutObj: null,
        start() {
          const self = this;
          let _num = self.num;
          self.timeoutObj && clearTimeout(self.timeoutObj);
          self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj);
          self.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            const data = {
              type: 999,
              roomId: this.ijt,
              message: "C7F6AE56ED1B4E18",
              form: "system",
            };
            this.webSocket.send(JSON.stringify(data)); // 心跳包
            _num--;
            //计算答复的超时次数
            if (_num === 0) {
              this.webSocket.colse();
            }
          }, self.timeout);
        },
      },
    };
  },
  created() {
    this.localVideoObj = this.$refs.screen_area;
  },
  mounted() {
    this.ijt = this.getUrlSearch("ijt");
    console.log(this.ijt);
    if ("WebSocket" in window) {
      this.createWebSocket();
    } else {
      /*浏览器不支持 WebSocket*/
      alert("您的浏览器不支持 WebSocket!");
    }
  },
  methods: {
    getMedia() {
      var self = this;
      self.getUserMedia();
      self.getUserMedia_Camera();
    },
    getUrlSearch(name) {
      // 未传参，返回空
      if (!name) return null;
      // 查询参数：先通过search取值，如果取不到就通过hash来取
      let after = window.location.search;
      after = after.substr(1) || window.location.hash.split("?")[1];
      // 地址栏URL没有查询参数，返回空
      if (!after) return null;
      // 如果查询参数中没有"name"，返回空
      if (after.indexOf(name) === -1) return null;

      const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      // 当地址栏参数存在中文时，需要解码，不然会乱码
      const r = decodeURI(after).match(reg);
      // 如果url中"name"没有值，返回空
      if (!r) return null;

      return r[2];
    },
    createWebSocket() {
      var self = this;
      try {
        if (typeof WebSocket == "undefined") {
          alert("您的浏览器不支持WebSocket");
          self.webSocket = null;
        } else {
          self.webSocket = new WebSocket(self.wsUrl);
          self.webSocket.onopen = self.open;
          self.webSocket.onerror = self.error;
          self.webSocket.onmessage = self.getMessage;
          window.onbeforeunload = function () {
            self.webSocket.close();
          };
          // this.init();
        }
      } catch (e) {
        console.log("catch");
        self.reconnect(this.wsUrl); //调用心跳
      }
    },
    reconnect(url) {
      var self = this;
      if (self.lockReconnect) {
        return true;
      }
      self.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      setTimeout(function () {
        self.createWebSocket(url);
        self.lockReconnect = false;
      }, 5000);
    },
    open: function () {
      const divscll = document.getElementById("message-content");
      divscll.scrollTop = divscll.scrollHeight;
      this.heartCheck.start(); //调用心跳
    },
    getMessage: function (evt) {
      // 收到服务器发送的消息后执行的回调
      var self = this;
      const dataStream = JSON.parse(evt.data);
      if (dataStream.type == 999) {
        if (dataStream.message.search("C7F6AE56ED1B4E18") == -1) {
          this.message_status_text = dataStream.message;
          // self.webSocket.send(JSON.stringify(evt.data));
        } else if (dataStream.ijt) {
          self.heartCheck.start(); //调用心跳
        }
      } else if (dataStream.type == 991 && dataStream.roomId == this.ijt) {
        const divscll = document.getElementById("message-content");
        divscll.scrollTop = divscll.scrollHeight;
        console.log("调用setMessageInnerHTML");
        self.setMessageInnerHTMLStudent(dataStream.message);
      } else if (dataStream.type == 900) {
        // this.message_status_text = dataStream.message;
        // self.webSocket.send(JSON.stringify(evt.data));
      } else if (dataStream.type == 6 && dataStream.roomId == this.ijt) {
        self.remoteAnswser_text = "";
        // document.getElementById("remoteAnswser").innerHTML = "";
        // if (self.localMediaStream != null && self.localCameraStream != null) {
          this.$notify({
            title: "成功",
            message: "对方已经接受，本地资源已准备就绪...3秒后连接！",
            type: "success",
          });
          setTimeout(function () {
            self.$notify({
              title: "成功",
              message: "正在连接中...3秒后通话！",
              type: "success",
            });
            setTimeout(function () {
              self.startLive();
              self.$notify({
                title: "成功",
                message: "链接成功！",
                type: "success",
              });
            }, 3000);
          }, 3000);
        // } else {
        //   this.$message.error("本地资源未准备就绪！");
        // }
      } else {
        console.log(
          "dataStream.action: " +
            dataStream.action +
            ";dataStream.roomid:" +
            dataStream.roomId
        );
        if (dataStream.action != undefined && dataStream.roomId != null) {
          if (dataStream.type == 101) {
            switch (dataStream.action) {
              case "answer":
                self.offerer_camera.setRemoteDescription(
                  new RTCSessionDescription(dataStream.answer)
                );
                self.createAnswerer_camera(dataStream.offer);
                break;
              case "candidate":
                self.offerer_camera.addIceCandidate(
                  new RTCIceCandidate(dataStream.candidate)
                );
                break;
            }
          } else if(dataStream.type == 103){
            switch (dataStream.action) {
              case "answer":
                self.offerer_screen.setRemoteDescription(
                  new RTCSessionDescription(dataStream.answer)
                );
                self.createAnswerer_screen(dataStream.offer);
                break;
              case "candidate":
                self.offerer_screen.addIceCandidate(
                  new RTCIceCandidate(dataStream.candidate)
                );
                break;
            }
          }
        } else {
          console.log("error");
        }
      }
    },
    close: function () {
      this.reconnect(this.wsUrl); //关闭连接重新连接
    },
    error: function () {
      const divscll = document.getElementById("message-content");
      divscll.scrollTop = divscll.scrollHeight;
    },
    init() {},
    WebSocketSend() {
      var self = this;
      const messageStr = this.message_input;
      if (messageStr.length != 0) {
        const message_data = {
          type: 990,
          roomId: this.ijt,
          message:
            "<strong></strong><span style='color: black;font-weight: bold'>" +
            messageStr +
            "</span>",
          form: "system",
        };
        // alert(JSON.stringify(message_data))
        this.webSocket.send(JSON.stringify(message_data));

        self.setMessageInnerHTMLTeacher(messageStr);
        this.message_input = "";
        //滚动条一直处于最下变
        const divscll = document.getElementById("message-content");
        divscll.scrollTop = divscll.scrollHeight;
      } else {
        this.$message.warning("不能发送空信息哦！");
      }
    },
    setMessageInnerHTMLTeacher(innerHTML) {
      var self = this;
      const temp =
        '<div align="right"><div style="background-color:rgb(152, 225, 101);width: auto;min-width: 20%;' +
        "text-align:right; display: inline-block;" +
        'margin:4px;padding-left: 4px;padding-right: 4px;border-radius: 5px">' +
        innerHTML +
        "</div></div>";
      console.log(temp);
      self.val = self.val + temp;
      self.message_content_text = self.val;
    },
    setMessageInnerHTMLStudent(innerHTML) {
      var self = this;
      const temp =
        '<div style="background-color:white;width: auto;min-width: 20%;text-align:left;' +
        'margin:4px;padding-left: 4px;padding-right: 4px;border-radius: 5px;display: inline-block;">' +
        innerHTML +
        "</div><br/>";
      console.log(innerHTML);
      self.val = self.val + temp;
      self.message_content_text = self.val;
    },
    isSupport() {
      try {
        let testRTCPeerConnection =
          window.RTCPeerConnection ||
          window.webkitRTCPeerConnection ||
          window.mozRTCPeerConnection ||
          window.RTCIceGatherer;
        let SessionDescription =
          window.RTCSessionDescription ||
          window.mozRTCSessionDescription ||
          window.webkitRTCSessionDescription;
        if (testRTCPeerConnection && SessionDescription) {
          return true;
        } else {
          throw "当前浏览器不支持该功能";
          // return;
        }
      } catch (error) {
        console.error("当前浏览器不支持该功能");
        return false;
      }
    },
    createQRCode() {
      window.open("qr.html?ijt=" + this.ijt);
    },
    openCamera() {
      // if (this.acctiveStream != 10) {
      if (this.localMediaStream == null && this.localCameraStream == null) {
        const data = {
          type: 5,
          roomId: this.ijt,
          form: "teacher",
          description: "openWebCamera",
        };
        console.log(data);
        this.webSocket.send(JSON.stringify(data));
      } else {
        this.$message.error("本地资源未准备就绪！");
      }
    },
    openRemoteVoice() {
      const data = { type: 2, roomId: this.ijt, form: "teacher" };
      this.webSocket.send(JSON.stringify(data));
      document.getElementById("remoteAudio").muted = false;
    },
    fullScreen() {
      const data = { type: 4, roomId: this.ijt, form: "teacher" };
      this.webSocket.send(JSON.stringify(data));
    },
    async getUserMedia() {
      if (this.isSupport()) {
        //获取声音信息
        const audioTrack = await navigator.mediaDevices.getUserMedia({
          //await
          audio: {
            echoCancellation: true,
            noiseSuppression: true,
            autoGainControl: true,
          },
        });
        //获取屏幕信息
        navigator.mediaDevices //2021/03/13
          .getDisplayMedia({
            audio: {
              echoCancellation: true,
              noiseSuppression: true,
              autoGainControl: true,
            },
            video: true,
          })
          .then(function (streamS) {
            // const local
            const localVideoObj = document.getElementById("screen_div");
            streamS.addTrack(audioTrack.getAudioTracks()[0]);
            localVideoObj.srcObject = streamS;
            localMediaStream = streamS;
          })
          .catch(function (error) {
            console.log("error" + error.message);
          });
        this.acctiveStream = 0;
        //把媒体流，发送给peer对象
      }
    },
    reloadRemote() {
      const data = { type: 1, roomId: this.ijt, form: "teacher" };
      this.webSocket.send(JSON.stringify(data));
      window.location.reload();
    },
    startLive() {
      // if (this.acctiveStream == 1) {
      this.offererPeer_camera(localCameraStream);
      this.offererPeer_screen(localMediaStream);
      // this.acctiveStream = 0;
      this.openRemoteVoice();
      this.currentSources_area = "当前直播窗口是：摄像头";
      // } else if (this.acctiveStream == 0) {
      // this.acctiveStream = 1;
      // this.openRemoteVoice();
      this.currentSources_area = "当前直播窗口是：屏幕分享";
      // } else {
      //   this.currentSources_area = "没有获取到任何媒体流！";
      //   // getUserMedia_Camera
      //   this.offererPeer(localCameraStream);
      //   this.acctiveStream = 0;
      //   this.openRemoteVoice();
      //   this.currentSources_area = "已经自动开启摄像头！";
      // }
    },
    async getUserMedia_Camera() {
      var self = this;
      if (this.isSupport()) {
        //获取声音信息
        navigator.mediaDevices
          .getUserMedia({
            audio: {
              echoCancellation: true,
              noiseSuppression: true,
              autoGainControl: true,
            },
            video: true,
          })
          .then(function (stream) {
            const localVideoObj = document.getElementById("vi");
            // this.localVideoObj = self.$refs.local_video;
            // this.localVideoObj = this.$refs.local_video;
            localVideoObj.srcObject = stream;
            localCameraStream = stream;
            //把媒体流，发送给peer对象
            // self.acctiveStream = 1;
          })
          .catch(function (error) {
            console.log("error " + error.message);
          });
      }
    },
    offererPeer_camera(video_stream) {
      var self = this;
      //core
      self.offerer_camera = new RTCPeerConnection(common.CommonObj.ICEServer);
      console.log(video_stream);
      video_stream.getTracks().forEach(
        (track) => self.offerer_camera.addTrack(track, video_stream),
        function () {
          console.log("ero");
        }
      );
      self.offerer_camera
        .createOffer()
        .then(function (offer) {
          console.log(
            "---> Creating new description object to send to remote peer"
          );
          return self.offerer_camera.setLocalDescription(offer);
        })
        .then(function () {
          console.log("建立offererPeer_camera");
          self.sendMessage({
            // type: "video-offer",
            type: 101,
            action: "create",
            offer: self.offerer_camera.localDescription,
            roomId: self.ijt,
            from: "teacher",
            to: "student",
            // type: 100
          });
        })
        .catch(function (error) {
          console.log("error:" + error.message);
        });
    },
    offererPeer_screen(video_stream) {
      var self = this;
      //core
      self.offerer_screen = new RTCPeerConnection(common.CommonObj.ICEServer);
      console.log(video_stream);
      video_stream.getTracks().forEach(
        (track) => self.offerer_screen.addTrack(track, video_stream),
        function () {
          console.log("ero");
        }
      );
      self.offerer_screen
        .createOffer()
        .then(function (offer) {
          console.log(
            "---> Creating new description object to send to remote peer"
          );
          return self.offerer_screen.setLocalDescription(offer);
        })
        .then(function () {
          console.log("建立offererPeer_screen");
          self.sendMessage({
            // type: "video-offer",
            type: 103,
            action: "create",
            offer: self.offerer_screen.localDescription,
            roomId: self.ijt,
            from: "teacher",
            to: "student",
            // type: 100
          });
        })
        .catch(function (error) {
          console.log("error:" + error.message);
        });
    },
    sendMessage(data) {
      this.webSocket.send(JSON.stringify(data));
    },

    createAnswerer_camera(offer) {
      console.log(offer);
      this.offerer_camera.createAnswer(
        function (answer) {
          this.offerer_camera.setLocalDescription(answer);
          this.sendMessage({
            action: "answer",
            answer: answer,
            roomId: this.ijt,
            from: "student",
            to: "teacher",
            type: 101,
          });
        },
        function (error) {
          console.log("error:" + error.message);
        },
        common.mediaConstraints
      );
      this.offerer_camera.onicecandidate = function (event) {
        if (!event || !event.candidate) return;
        this.sendMessage({
          action: "candidate",
          candidate: event.candidate,
          roomId: this.ijt,
          from: "teacher",
          to: "student",
          type: 100,
        });
      };
      this.offerer_camera.onaddstream = function (event) {
        const remoteAudio_div = document.getElementById("remoteAudio");
        remoteAudio_div.srcObject = event.stream;
        remoteAudio_div.play();
      };
    },
    createAnswerer_screen(offer) {
      console.log(offer);
      this.offerer_screen.createAnswer(
        function (answer) {
          this.offerer_screen.setLocalDescription(answer);
          this.sendMessage({
            action: "answer",
            answer: answer,
            roomId: this.ijt,
            from: "student",
            to: "teacher",
            type: 103,
          });
        },
        function (error) {
          console.log("error:" + error.message);
        },
        common.mediaConstraints
      );
      this.offerer_screen.onicecandidate = function (event) {
        if (!event || !event.candidate) return;
        this.sendMessage({
          action: "candidate",
          candidate: event.candidate,
          roomId: this.ijt,
          from: "teacher",
          to: "student",
          type: 102,
        });
      };
      this.offerer_screen.onaddstream = function (event) {
        const remoteAudio_div = document.getElementById("remoteAudio");
        remoteAudio_div.srcObject = event.stream;
        remoteAudio_div.play();
      };
    },
    hangup() {
      console.log("Ending call");
      this.offerer_camera.close();
      this.offerer_screen.close();
      this.offerer_camera = null;
      this.offerer_screen = null;

      const data = { type: 3, roomId: this.ijt, form: "teacher" };
      this.webSocket.send(JSON.stringify(data));
    },
  },
};
</script>

<style>
.el-main-style {
  display: flex;
  width: 100vw;
  height: 84vh;
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  margin: 0;
  padding: 0;
  background-color: rgb(242, 222, 177);
}
.el-aside-left:focus {
  width: 3px;
}

video::selection {
  border: none;
}

.el-menu-item-left {
  font-size: 12px;
  height: 10vh !important;
  background-color: rgb(46, 50, 61) !important;
  color: white !important;
}
.el-menu-item-left:hover,
.el-menu-item-left.is-active {
  cursor: pointer;
  font-size: 18px;
  background-color: rgb(38, 41, 51) !important;
  color: white !important;
}
</style>