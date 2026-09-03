<template>
    <el-container>
        <el-header id="nav" >
            <Header />
        </el-header>
        <!-- <el-aside >
            <el-menu background-color="#545c64"
      text-color="#fff" style="width: 12vw;height: 88vh;">
          <el-menu-item class="left-menu-btn" index="1">
              教师账户管理
          </el-menu-item>
          <el-menu-item class="left-menu-btn" index="2">
              学生账户管理
          </el-menu-item>
          <el-menu-item class="left-menu-btn" index="3">
              分类课程管理
          </el-menu-item>
          <el-menu-item class="left-menu-btn" index="4">
              教师课程管理
          </el-menu-item>
            </el-menu>
        </el-aside> -->
        <el-tabs tab-position="left" style="height: 88vh;">
            <el-tab-pane label="教师账户管理">
                <TeacherManager/>
            </el-tab-pane>
            <el-tab-pane label="学生账户管理">
                <StudentManager/>
            </el-tab-pane>
            <el-tab-pane label="分类课程管理">
                <CategoryCourse/>
            </el-tab-pane>
            <el-tab-pane label="教师课程管理">
                <TeacherCourse/>
            </el-tab-pane>
        </el-tabs>
    </el-container>
</template>

<script>
import Header from "@/components/Header/Header.vue";
import TeacherCourse from "@/components/Admin/teacherCourse.vue";
import TeacherManager from "@/components/Admin/teacherManager.vue";
import CategoryCourse from "@/components/Admin/categoryCourse.vue";
import StudentManager from "@/components/Admin/studentManager.vue";
export default {
    name: "App",
    components: {
        Header,
        TeacherCourse,
        TeacherManager,
        CategoryCourse,
        StudentManager
    }
}
</script>


<style>
.left-menu-btn{
    width: 12vw
}
.left-menu-btn-inside{
    width: 8vw !important;
}
</style>