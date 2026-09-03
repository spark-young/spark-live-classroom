<template>
    <div class="course-manager" v-if="isTeacher">
        <CourseListTeacher/>
    </div>
    <div v-else>
        <CourseListStudent/>
    </div>
</template>

<script>
import CourseListTeacher from '@/components/course/courseListTeacher.vue'
import CourseListStudent from '@/components/course/courseListStudent.vue'
export default {
    name: "App",
    components: {
        CourseListTeacher,
        CourseListStudent
    },
    data() {
        return {
            isTeacher: true,
        }
    },
    mounted() {
        var self = this;
        self.isTeacher = self.$store.state.roleId == 1;
    }
}
</script>

<style>
.course-manager{
    width: 62vw;
    height: 80vh;
    overflow: scroll;
}
.course-manager::-webkit-scrollbar{
    width: 0;
}
</style>