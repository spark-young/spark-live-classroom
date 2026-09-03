<template>
    <el-container>
        <el-header id="nav">
            <MainHeader/>
        </el-header>
    <el-container>
        <el-main>
            {{courseId}}
        </el-main>
    </el-container>
    </el-container>
</template>

<script>
import MainHeader from '@/components/Header/MainHeader.vue';
export default {
    name: "App",
    components: {
        MainHeader
    },
    data() {
        return {
            courseId: -1
        }
    },
    mounted() {
        var self = this;
        self.courseId = window.location.url.split('=')[1];
        console.log("courseId="+self.courseId);
    }
}
</script>