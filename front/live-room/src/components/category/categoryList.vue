<template>
  <el-menu
    v-loading="loading"
    class="el-menu-style"
    text-color="rgb(216, 216, 216)"
  >
    <el-menu-item
      class="el-menu-item-category"
      @click="changeCourse()"
      index="-1"
    >
      <span slot="title">全部课程</span>
    </el-menu-item>
    <el-menu-item
      class="el-menu-item-category"
      @click="changeCourseType(item.id)"
      v-for="(item, i) in categoryList"
      :key="i"
      :index="i + ''"
    >
      <el-tooltip class="item" effect="dark" content="Top Left 提示文字" placement="top-start">
        <span slot="title">{{ categoryList[i].title }}</span>
      </el-tooltip>

      <span ref="menu_item" slot="title">{{ categoryList[i].title }}</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      categoryList: [],
      loading: true,
    };
  },
  mounted() {
    var self = this;
    self.$http.get(self.$api.CategoryList).then((res) => {
      var categoryData = JSON.parse(res.data.data);
      self.categoryList = categoryData;
      console.log(self.categoryList);
    });
    self.loading = false;
  },
  methods: {
    changeCourseType(id) {
      var self = this;
      if (window.location.href.split("=")[1] != id)
        self.$router.push("/course/category?typeId=" + id);
    },
    changeCourse() {
      var self = this;
      if (window.location.href.split("#")[1] != "/") self.$router.push("/");
    },
  },
};
</script>

<style>
.el-menu-style {
  background: transparent;
  color: white;
  background: transparent !important;
  border: none !important;
}
.el-menu-item-category {
  border-radius: 5px;
  margin: 1vh;
  font-size: 12px;
  background: transparent;
  height: 6vh !important;
  line-height: 6vh !important;
  color: white;
}
/* 鼠标覆盖的样式 */
.el-menu-item-category:hover {
  cursor: pointer;
  font-size: 18px;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: white !important;
  box-shadow: 0px 0 10px #ffffff;
}
/* 被选中的样式 */
.el-menu-item-category.is-active {
  cursor: pointer;
  font-size: 18px;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: white !important;
}
</style>