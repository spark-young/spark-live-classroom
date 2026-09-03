<template>
<div>
    <vue-aliplayer-v2 :source="source" ref="VueAliplayerV2" :options="options" />
    <p class="remove-text" v-if="!show">播放器已销毁!</p>
    <el-button @click="onFullScreen()">全屏</el-button>
    <el-button @click="replay()">重播</el-button>
</div>
</template>

<script>
// import ffmpeg from 'ffmpeg'
var ffmpeg = require('ffmpeg');
export default {
    data() {
        return {
            options: {
                width: "640px",
                height: "320px",
                // source:'//player.alicdn.com/video/aliyunmedia.mp4',
                isLive: true, //切换为直播流的时候必填
                // format: 'm3u8'  //切换为直播流的时候必填
                preload: true,
                showBuffer: true,
                useH5Prism: true,
                controlBarVisibility: "click",
                // autoPlayDelay: 10,
                // autoPlayDelayDisplayText: "wait a second!",
                enableSystemMenu: true
            },
            // source: 'rtmp://119.29.61.66:1935/live/a',
            source: 'http://119.29.61.66:8080/live/a.flv',
            // source: 'http://119.29.61.66:8080/live/a.m3u8',
            // source: 'http://localhost:8848/live/a.m3u8',
            show: true
        }
    },
    created: function () {
        // var types = ["video/webm",
        //     "audio/webm",
        //     "video/webm;codecs=vp8",
        //     "video/webm;codecs=daala",
        //     "video/webm;codecs=h264",
        //     "audio/webm;codecs=opus",
        //     "video/mpeg"
        // ];

        // for (var i in types) {
        //     console.log("Is " + types[i] + " supported? " + (MediaRecorder.isTypeSupported(types[i]) ? "Maybe!" : "Nope :("));
        // }
        // this.$refs.VueAliplayerV2.replay();

        try {
            var process = new ffmpeg('../assets/000.flv');
            process.then(function (video) {
                console.log('The video is ready to be processed'+video);
            }, function (err) {
                console.log('Error: ' + err);
            });
        } catch (e) {
            console.log(e.code);
            console.log(e.msg);
        }
    },
    methods: {
        onFullScreen() {
            this.$refs.VueAliplayerV2.requestFullScreen();
        },
        replay() {
            this.$refs.VueAliplayerV2.replay();
        }
    }
}
</script>

<style>
* {
    margin: 0;
    padding: 0;
}
</style>
