<template>
    <el-container>
        <el-main class="main-style">
            <el-table :data="teacherList"
    style="width: 100%"
    :row-class-name="tableRowClassName">
                <el-table-column prop="name" label="用户名">
                    
                </el-table-column>
                <el-table-column prop="email" label="邮箱">
                    
                </el-table-column>
                <el-table-column prop="mobile" label="电话">
                    
                </el-table-column>
                <el-table-column prop="status" label="状态">
                    <template #default="scope">
                    {{teacherList[scope.$index].status?"启用":"禁用"}}
                    <el-switch v-model="teacherList[scope.$index].status" v-loading="statusLoading" @change="submit(scope.$index)"></el-switch>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "App",
    data() {
        return {
            teacherList: [],
            statusLoading: false
        }
    },
    mounted() {
        var self = this;
        self.$http.get(self.$api.AdminTeacherList).then((res) => {
            if(res.data.success){
                console.log(res.data.data);
                self.teacherList = JSON.parse(res.data.data);
                console.log(self.teacherList)
            }
        });
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        if (row.status) {
            return ''; 
        } else {
          return 'warning-row';
        }
      },
      submit(index) {
          var self = this;
          self.statusLoading = true;

          var url = self.$api.AdminChangeStatus.url;
          self.$api.AdminChangeStatus.url = url.split("=")[0] + "=" + self.teacherList[index].id;
          self.$http.get(self.$api.AdminChangeStatus).then((res) => {
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
.main-style{
    height: 88vh;
}
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>