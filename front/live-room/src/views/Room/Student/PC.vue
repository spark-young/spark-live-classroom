<template>
  <el-container
    style="margin: 0; padding: 0; width: 100%; height: 100%; display: flex"
  >
    <el-header id="nav">
      <TapBar />
    </el-header>
    <el-container>
      <el-main class="el-main-style">
        <div
          id="p2"
          title="教师屏幕"
          style="width: 100%; height: 100%; padding: 1px"
        >
          <p style="height=10%">教师屏幕</p>
          <video
            id="peer1-to-peer2"
            autoplay="autoplay"
            loop="loop"
            muted="muted"
            poster="../../../images/main.jpg"
            webkit-playsinline="true"
            playsinline=""
            preload="auto"
            x5-video-player-type="h5"
            style="width: 98%; height: 90%"
          ></video>
        </div>
        <div style="width: 40%; justify-content: center">
          <div
            id="p"
            title="教师摄像头"
            style="width: 98%; height: 50%; margin-left: 1%"
          >
            <p line-height="10%">教师相机</p>
            <video
              class="video-item"
              id="vi_teacher"
              ref="screen_video"
              autoplay="autoplay"
              loop="loop"
              muted="false"
              poster="../../../images/left2.jpg"
              controlsList="nodownload"
              style="width: 100%; height: 95%"
            ></video>
          </div>
          <div
            id="p1"
            title="本地相机"
            style="width: 98%; height: 50%; margin-left: 1%"
          >
            <p line-height="10%">本地相机</p>
            <video
              class="video-item"
              id="vi_student"
              ref="local_video"
              autoplay="autoplay"
              loop="loop"
              muted="false"
              poster="../../../images/left1.jpg"
              controlsList="nodownload"
              style="width: 100%; height: 95%"
            ></video>
          </div>
        </div>
      </el-main>

      <el-aside
        width="20%"
        height="100%"
        style="padding: 2px; background-color: rgb(238, 241, 246)"
      >
        <div
          style="
            width: 100%;
            height: 85%;
            max-height: 85%;
            position: relative;
            top: 0px;
          "
        >
          <div class="translate-area" @dblclick="openNote()">
            {{ translateContent }}
          </div>
          <div
            id="message-status"
            style="
              margin-top: 0px;
              font-size: 1.4rem;
              border: 0px solid #cad9ea;
              width: 100%;
              height: 4%;
              text-align: center;
            "
            v-html="message_status_text"
          ></div>
          <div
            id="message-content"
            data-options="multiline:true"
            style="
              margin-top: 2%;
              line-height: 4vh;
              width: 100%;
              height: 60vh;
              justify-content: center;
              overflow: scroll;
              text-align: left;
            "
            v-html="message_content_text"
            @mouseup="translate()"
          ></div>
        </div>
        <el-input
          id="text-input"
          v-model="message_input"
          placeholder="请输入内容"
          @keyup.enter.native="WebSocketSend"
          style="
            position: absolute;
            width: 20%;
            height: 4%;
            right: 0%;
            bottom: 6vh;
            font-size: 18px;
            background-color: rgb(238, 241, 246);
          "
        />
      </el-aside>
      <el-dialog
        title="随堂笔记"
        class="note-dialog"
        :visible.sync="noteDialogVisible"
        width="30%"
        center
        :show-close="false"
      >
        <el-form>
          <el-form-item prop="noteContent">
            
            <quill-editor class="editor my-editor"
            v-model="noteContent" 
            ref="myQuillEditor" 
            :options="editorOption" 
            @blur="onEditorBlur($event)" @focus="onEditorFocus($event)"
            @change="onEditorChange($event)"
>
        </quill-editor>
            
          </el-form-item>
          <div style="position: absolute; top: 2vh; right: 2vw">
          <el-button type="info" @click="noteDialogVisible = false"
            >取消</el-button
          >
          <el-button type="primary" @click="submitNote()">记录</el-button>
        </div>
        </el-form>
      </el-dialog>
      <el-dialog
        ref="dlg_calling"
        title="对方邀请您进行通话"
        :visible.sync="isReceive"
        width="40%"
        height="40%"
        center
      >
        <span slot="footer" class="dialog-footer">
          <el-button @click="getUserMedia(2)">视频通话</el-button>
          <el-button type="primary" @click="getUserMedia(1)"
            >语音通话</el-button
          >
        </span>
      </el-dialog>
      <el-dialog
        ref="dlg_calling"
        title="对方请求您全屏观看"
        :visible.sync="isFullScreen"
        width="40%"
        height="40%"
        center
      >
        <span slot="footer" class="dialog-footer">
          <el-button @click="fullScreen">好的</el-button>
        </span>
      </el-dialog>
    </el-container>
  </el-container>
</template>
<script>
import common from "@/assets/js/common.js";
import TapBar from "@/components/Header/TapBar.vue";
import "@/assets/js/adapter-latest.js";
import axios from "axios";
export default {
  components: {
    TapBar,
  },
  data() {
    return {
      noteDialogVisible: false,
      noteContent: "",
      editorOption:{},

      message_input: "",
      isReceive: false,
      isFullScreen: false,
      message_status_text: "加载中",
      message_content_text: "",
      message: "",
      offerer: "",
      answerer: "",
      answerer_camera: "",
      answerer_screen: "",
      offererToAnswerer_Screen: null,
      offererToAnswerer_Camera: null,
      lockReconnect: false, //避免重复连接
      wsUrl: "wss://spark-young.top/e3/",
      webSocket: null,
      val: "",
      ijt: "",
      localAudioSteam: null,
      mediaConstraints: {
        optional: [],
        mandatory: { OfferToReceiveAudio: true, OfferToReceiveVideo: true },
      },
      translateContent: "",
      //心跳检测
      heartCheck: {
        timeout: 10000, //每隔三秒发送心跳
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
            console.log(self.webSocket);
            self.webSocket.send(JSON.stringify(data)); // 心跳包
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
  mounted() {
    var self = this;
    self.ijt = self.getUrlSearch("ijt");
    if ("WebSocket" in window) {
      self.createWebSocket();
    } else {
      /*浏览器不支持 WebSocket*/
      alert("您的浏览器不支持 WebSocket!");
    }
    self.jump();
    self.$message({
      duration: 5000,
      message: "双击右上角灰色区域可以进行随堂记录哦~",
    });
  },
  methods: {
    openNote() {
      var self = this;
      self.noteDialogVisible = true;
    },

    onEditorBlur(){
        },
        onEditorFocus(){//获得焦点事件
        },
        onEditorChange(){//内容改变事件
        },

    submitNote() {
      var self = this;
      let NoteDTO = {
        sid: self.$store.state.id,
        content: self.noteContent,
      };
      self.$http.post(self.$api.AddNote, NoteDTO).then((res) => {
        if (res.data.success) {
          self.$message.success("添加笔记成功！");
          self.noteDialogVisible = false;
        } else {
          self.$message.error("添加失败，请重试！");
        }
      });
    },
    jump() {
      //1、识别用户设备是PC端还是手机端
      //声明要识别的手机端设备
      var devices = ["iPhone", "Android", "Windows Phone"];
      //获取当前用户设备信息
      var userAgent = window.navigator.userAgent;

      //2、识别用户PC端还是手机端
      for (let i = 0; i < devices.length; i++) {
        //找到对应手机端设备的信息后跳转后手机端指定页面
        if (userAgent.indexOf(devices[i]) != -1) {
          //当前跳转为重定向跳转url会改变，如果想在Url不改变的情况下进行跳转可考虑使用iframe，js控制iframe的src。
          self.$router.push("/room/student/mobile?_ijt=" + self.ijt);
        }
      }
      //当循环结束后找不到对应的手机端设备信息则保持在默认的PC端页面上，即不跳转。
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
        } else {
          self.webSocket = new WebSocket(self.wsUrl);
          self.webSocket.onopen = self.open;
          self.webSocket.onerror = self.error;
          self.webSocket.onmessage = self.getMessage;
          window.onbeforeunload = function () {
            self.webSocket.close();
          };
          // self.init();
        }
      } catch (e) {
        console.log("catch");
        self.reconnect(self.wsUrl); //调用心跳
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
      var self = this;
      const divscll = document.getElementById("message-content");
      divscll.scrollTop = divscll.scrollHeight;
      self.heartCheck.start(); //调用心跳
      const messageStr = "连线成功！";
      const data = {
        type: 999,
        roomId: this.ijt,
        message: "<span style='color: green'>" + messageStr + "</span>",
        form: "system",
      };
      self.webSocket.send(JSON.stringify(data));
    },
    close: function () {
      var self = this;
      const messageStr = "对方，离开直播间！";
      const data = {
        type: 999,
        roomId: this.ijt,
        message: "<span style='color: green'>" + messageStr + "</span>",
        form: "system",
      };
      self.webSocket.send(JSON.stringify(data));
      self.reconnect(self.wsUrl); //关闭连接重新连接
    },
    getMessage: function (evt) {
      var self = this;
      // 收到服务器发送的消息后执行的回调
      let dataStream = JSON.parse(evt.data);
      if (dataStream.type == 1 && dataStream.roomId == this.ijt) {
        window.location.reload();
      } else if (dataStream.type == 2 && dataStream.roomId == this.ijt) {
        document.getElementById("peer1-to-peer2").muted = false;
      } else if (dataStream.type == 3 && dataStream.roomId == this.ijt) {
        self.answerer_camera.close();
        self.answerer_camera = null;
        self.answerer_screen.close();
        self.answerer_screen = null;
        alert("对方已经挂断！");
        self.$router.push({ name: "Home" });
      } else if (dataStream.type == 4) {
        // this.$refs.dlg_full_screen.dialog('open').dialog('center');
        if (dataStream.roomId == this.ijt) {
          self.isFullScreen = true;
        } else {
          console.log("不是给我发的哟！");
        }
      } else if (dataStream.type == 5) {
        // this.$refs.dlg_calling.dialog('open').dialog('center');
        if (dataStream.roomId == this.ijt) {
          self.isReceive = true;
        } else {
          console.log("不是给我发的哟！");
        }
      } else if (dataStream.type == 900 && dataStream.roomId == this.ijt) {
        self.message_status_text = dataStream.message;
      } else if (dataStream.type == 990) {
        //990为教师端发送消息
        // const divscll = document.getElementById("message-content");
        // divscll.scrollTop = divscll.scrollHeight;
        self.setMessageInnerHTMLTeacher(dataStream.message);
      } else if (dataStream.type == 999) {
        if (dataStream.message.search("C7F6AE56ED1B4E18") == -1) {
          this.message_status_text = dataStream.message;
        } else if (dataStream.roomId == this.ijt) {
          self.heartCheck.start(); //调用心跳
        }
      } else {
        if (dataStream.action != undefined && dataStream.roomId != null) {
          if (dataStream.roomId == this.ijt) {
            // this.$message.success("接收视频通话成功" + dataStream.action);
            if (dataStream.type == 101 || dataStream.type == 100) {
              console.log("走到这里了");
              switch (dataStream.action) {
                case "create":
                  self.answererPeer_camera(dataStream.offer);
                  break;
                case "candidate":
                  self.answerer_camera.addIceCandidate(
                    new RTCIceCandidate(dataStream.candidate)
                  );
                  break;
              }
            } else if (dataStream.type == 103 || dataStream.type == 102) {
              console.log("走到这里了");
              switch (dataStream.action) {
                case "create":
                  self.answererPeer_screen(dataStream.offer);
                  break;
                case "candidate":
                  // self.answerer_camera.addIceCandidate(
                  self.answerer_screen.addIceCandidate(
                    new RTCIceCandidate(dataStream.candidate)
                  );
                  break;
              }
            }
          } else {
            console.log("不是给我发的消息哦");
          }
        }
      }
    },
    error: function () {
      const divscll = document.getElementById("message-content");
      divscll.scrollTop = divscll.scrollHeight;
    },
    WebSocketSend(e) {
      var self = this;
      const messageStr = self.message_input;
      if (messageStr.length != 0) {
        const message_data = {
          type: 991,
          roomId: this.ijt,
          message:
            "<strong></strong><span style='color: black;font-weight: bold'>" +
            messageStr +
            "</span>",
          form: "system",
        };
        // alert(JSON.stringify(message_data))
        self.webSocket.send(JSON.stringify(message_data));

        self.setMessageInnerHTMLStudent(messageStr);
        self.message_input = "";
        //滚动条一直处于最下变
        const divscll = document.getElementById("message-content");
        divscll.scrollTop = divscll.scrollHeight;
      } else {
        this.$message.warning("不能发送空消息哦！");
      }
    },
    setMessageInnerHTMLStudent(innerHTML) {
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
    setMessageInnerHTMLTeacher(innerHTML) {
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
    async getUserMedia(tag) {
      var self = this;
      if (self.isSupport()) {
        if (tag == 1) {
          //获取声音信息
          navigator.mediaDevices
            .getUserMedia({
              audio: {
                echoCancellation: true,
                noiseSuppression: true,
                autoGainControl: true,
              },
              video: false,
            })
            .then(function (stream) {
              // document.getElementById("localhostVideo").srcObject=stream;
              self.localAudioSteam = stream;
              console.log(
                "stream is" +
                  stream +
                  " and localAudioSteam is " +
                  self.localAudioSteam
              );
            })
            .catch(function (error) {
              console.log("error:" + error.message);
            });
          // self.$refs.dlg_calling.dialog("close");
          self.isReceive = false;
        } else {
          //获取声音信息
              console.log("获取声音信息")
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
              // document.getElementById("localhostVideo").srcObject=stream;
              const localVideoObj = document.getElementById("vi_student");
              localVideoObj.srcObject = stream;
              self.localAudioSteam = stream;
              console.log(
                "stream is" +
                  stream +
                  " and localAudioSteam is " +
                  self.localAudioSteam
              );
            })
            .catch(function (error) {
              console.log("error:" + error.message);
            });

          // self.$refs.dlg_calling.dialog("close");
          self.isReceive = false;
        }

        const data = {
          type: 6,
          roomId: self.ijt,
          form: "student",
          description: "openWebCamera",
        };
        console.log(data);
        self.webSocket.send(JSON.stringify(data));
      }
    },
    sendMessage(data) {
      this.webSocket.send(JSON.stringify(data));
    },
    answererPeer_camera(offer) {
      //core
      var self = this;
      self.answerer_camera = new RTCPeerConnection(common.CommonObj.ICEServer);
      self.localAudioSteam.getTracks().forEach(
        (track) => self.answerer_camera.addTrack(track, self.localAudioSteam),
        function (error) {
          console.log("error:" + error.message);
        }
      );
      self.answerer_camera.onaddstream = function (event) {
        console.log("已连接教师camera" + JSON.stringify(event.stream));
        self.offererToAnswerer_Camera = document.getElementById("vi_teacher");
        console.log(
          "offererToAnswerer_Camera is " + self.offererToAnswerer_Camera
        );
        self.offererToAnswerer_Camera.srcObject = event.stream;
        self.offererToAnswerer_Camera.play();
      };
      self.answerer_camera.onicecandidate = function (event) {
        if (!event || !event.candidate) return;
        self.sendMessage({
          action: "candidate",
          candidate: event.candidate,
          roomId: self.ijt,
          from: "student",
          to: "teacher",
          type: 101,
        });
      };
      const desc = new RTCSessionDescription(offer);
      self.answerer_camera.setRemoteDescription(desc);
      self.answerer_camera.createAnswer(
        function (answer) {
          self.answerer_camera.setLocalDescription(answer);
          self.sendMessage({
            action: "answer",
            answer: answer,
            roomId: self.ijt,
            from: "student",
            to: "teacher",
            type: 101,
          });
        },
        function (error) {
          console.log("error:" + error.message);
        },
        self.mediaConstraints
      );
    },
    answererPeer_screen(offer) {
      //core
      var self = this;
      self.answerer_screen = new RTCPeerConnection(common.CommonObj.ICEServer);
      self.localAudioSteam.getTracks().forEach(
        (track) => self.answerer_screen.addTrack(track, self.localAudioSteam),
        function (error) {
          console.log("error:" + error.message);
        }
      );
      self.answerer_screen.onaddstream = function (event) {
        console.log("已连接教师screen" + JSON.stringify(event.stream));
        self.offererToAnswerer_Screen = document.getElementById(
          "peer1-to-peer2"
        );
        console.log(
          "offererToAnswerer_Screen is " + self.offererToAnswerer_Screen
        );
        self.offererToAnswerer_Screen.srcObject = event.stream;
        self.offererToAnswerer_Screen.play();
      };
      self.answerer_screen.onicecandidate = function (event) {
        if (!event || !event.candidate) return;
        self.sendMessage({
          action: "candidate",
          candidate: event.candidate,
          roomId: self.ijt,
          from: "student",
          to: "teacher",
          type: 103,
        });
      };
      const desc = new RTCSessionDescription(offer);
      self.answerer_screen.setRemoteDescription(desc);
      self.answerer_screen.createAnswer(
        function (answer) {
          self.answerer_screen.setLocalDescription(answer);
          self.sendMessage({
            action: "answer",
            answer: answer,
            roomId: self.ijt,
            from: "student",
            to: "teacher",
            type: 103,
          });
        },
        function (error) {
          console.log("error:" + error.message);
        },
        self.mediaConstraints
      );
    },
    fullScreen() {
      document.getElementById("peer1-to-peer2").webkitRequestFullScreen();
      this.isFullScreen = false;
      // $('#dlg1').dialog('close');
      // screen.orientation.lock('landscape');
    },
    translate() {
      var self = this;
      let word = window.getSelection() + "";

      // var url = self.$api.TianAPIWord.url;
      // self.$api.TianAPIWord.url = url.split("word=")[0] + "word=" + word;

      if (word != "") {
        console.log(word.split(" ").length);
        if (word.split(" ").length > 2) {
          axios
            .post(
              "https://api.tianapi.com/txapi/fanyi/index?key=8c89da33c0ca5eaf95ec25e1abc32dd6&text=" +
                word,
              {
                name: "Javan",
                website: "www.javanx.cn",
              }
            )
            .then((res) => {
              self.translateContent = res.data.newslist[0].dst;
            })
            .catch((e) => console.log(e + "非单词不显示"));
        } else {
          axios
            .post(
              "https://api.tianapi.com/txapi/enwords/index?key=8c89da33c0ca5eaf95ec25e1abc32dd6&word=" +
                word,
              {
                name: "Javan",
                website: "www.javanx.cn",
              }
            )
            .then((res) => {
              self.translateContent = res.data.newslist[0].content;
              // self.$message({
              //   duration: 3000,
              //   message: res.data.newslist[0].content,
              //   type: "success",
              // });
            })
            .catch((e) => console.log(e + "非单词不显示"));
        }
        // self.$webhttp.get(self.$api.TianAPIWord).then((res) => {
        //   self.$message({
        //     duration: 3000,
        //     message: res.data.newslist[0].content,
        //     type: "success",
        //   });
        // }).catch((e) => console.log("非单词不显示"));
      }
    },
  },
};
</script>

<style>
.el-main-style {
  display: flex;
  width: 100vw;
  height: 88vh;
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  margin: 0;
  padding: 0;
  background-color: rgb(242, 222, 177);
}
.translate-area {
  margin-top: 1%;
  padding: 2%;
  font-weight: bold;
  border-radius: 10px;
  background: rgb(212, 212, 212);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
  height: 20%;
}

.note-dialog .el-dialog{
    width: 94vw !important;
    height: auto;
}
.my-editor{
    width: 90vw;
    height: 40vh;
    margin-bottom: 2vh;
}
</style>