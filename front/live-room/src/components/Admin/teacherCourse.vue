<template>
    <el-container>
        <!-- <el-aside class="aside-style">

        </el-aside>
        <el-main class="main-style">
            
        </el-main> -->
        <el-main class="main-style">
            <el-tabs tab-position="right" @tab-click="handleClick">
                <el-tab-pane v-for="(item, i) in teacherList"
                :key="i"
                :index="i" :label="teacherList[i].name">
                <el-table :data="courseList" class="table-style">
                    <el-table-column prop="cname" label="课程名">

                    </el-table-column>
                    <el-table-column prop="typeName" label="分类">

                    </el-table-column>
                    <el-table-column prop="description" label="课程描述">

                    </el-table-column>
                    <el-table-column prop="status" label="状态">
                    <template #default="scope">
                    {{courseList[scope.$index].status?"启用":"禁用"}}
                    <el-switch v-model="courseList[scope.$index].status" v-loading="statusLoading" @change="submit(scope.$index)"></el-switch>
                    </template>
                </el-table-column>
                </el-table>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "App",
    data() {
        return {
            teacherList: [],
            courseList: [],
            statusLoading: false,
        }
    },
    mounted() {
        var self = this;
        self.$http.get(self.$api.AdminTeacherList).then((res) => {
            if(res.data.success){
                self.teacherList = JSON.parse(res.data.data);
            }
        });
        setTimeout(() => {
            self.getCourseList(0);
        },2000);
        
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        if (row.status) {
            return ''; 
        } else {
          return 'warning-row';
        }
      },
      handleClick(tab, event) {
          var self = this;
        // console.log(self.teacherList[Number(tab.index)].title)
        self.getCourseList(Number(tab.index));
      },
      getCourseList(index) {
          var self = this;
          var url = self.$api.AdminTeacherToCourseList.url;
          self.$api.AdminTeacherToCourseList.url = url.split("=")[0] + "=" + self.teacherList[index].id;
          self.$http.get(self.$api.AdminTeacherToCourseList).then((res) => {
              console.log(res.data.data)
              if(res.data.success){
                  self.courseList = JSON.parse(res.data.data);
              }
          });


      },
      submit(index) {
          var self = this;
          self.statusLoading = true;

          var url = self.$api.AdminChangeCourseStatus.url;
          self.$api.AdminChangeCourseStatus.url = url.split("=")[0] + "=" + self.courseList[index].id;
          self.$http.get(self.$api.AdminChangeCourseStatus).then((res) => {
              if(res.data.success){
                  self.$message.success("修改状态成功！");
              }
          });

          self.statusLoading = false;
      }
    },
}
</script>

<style>
.aside-style{
    width: 12vw !important;
    height: 88vh;
}
.main-style{
    height: 88vh;
}
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
.table-style{
    height: 81vh;
    overflow-y: scroll;
}
</style>