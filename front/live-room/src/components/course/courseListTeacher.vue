<template>
  <div>
    <div v-show="courseManagerShow" v-loading="courseLoading">
      <el-dialog
        :title="isAddCourse ? '创建课程' : '课程信息修改'"
        :visible.sync="editdialogVisible"
        :show-close="false"
      >
        <el-form
          :model="courseInfo"
          :rules="courseInfoRules"
          ref="courseInfoForm"
        >
          <el-form-item prop="courseTitle">
            <el-input
              class="el-dialog-input"
              placeholder="请输入课程标题"
              v-model="courseInfo.courseTitle"
            ></el-input>
          </el-form-item>
          <el-form-item prop="courseDescription">
            <el-input
              class="courseInfo-description"
              type="textarea"
              :rows="8"
              :validate-event="true"
              placeholder="请输入课程描述"
              v-model="courseInfo.courseDescription"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="typeId">
            选择分类：
            <el-select v-model="courseInfo.typeId" placeholder="请选择">
              <el-option
                v-for="(item, i) in typeList"
                :key="i"
                :label="item.title"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="subCount">
            <el-slider
              v-model="courseInfo.subCount"
              show-input
              :min="isAddCourse ? 1 : curCount"
              :max="20"
            ></el-slider>
          </el-form-item>
          <el-form-item>
            <el-button type="info" @click="closeCourseInfo('courseInfoForm')"
              >取消</el-button
            >
            <el-button
              type="primary"
              @click="submitCourseInfo('courseInfoForm')"
              :loading="submitCourseInfoLoading"
              >{{
                isAddCourse
                  ? submitCourseInfoLoading
                    ? "正在提交"
                    : "创建课程"
                  : submitCourseInfoLoading
                  ? "正在提交"
                  : "提交修改"
              }}</el-button
            >
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-dialog
        :append-to-body="true"
        :visible.sync="addOrEditChapterVisible"
        :show-close="false"
        :close-on-press-escape="false"
        :close-on-click-modal="false"
        :title="
          isAddChapter
            ? '您将在该章节点下创建新的章节点'
            : '您将修改该章节点信息'
        "
      >
        <div style="text-align: right; margin: 0">
          <el-form :model="ChapterData" :rules="ChapterDataRules" ref="ChapterDataForm">
            <!-- <el-form-item>
                      <h2>教师信息修改</h2>
                    </el-form-item> -->
            <el-form-item prop="title">
              <el-input
                class="el-chapter-add-input"
                placeholder="请输入章节标题"
                v-model="ChapterData.title"
              ></el-input>
            </el-form-item>
            <el-form-item prop="description">
              <el-input
                type="textarea"
                :rows="6"
                placeholder="请输入章节详情"
                v-model="ChapterData.description"
              >
              </el-input>
            </el-form-item>
            <el-form-item style="text-align: center">
              <el-popover
                placement="top"
                :width="550"
                trigger="manual"
                v-if="editChapterTime"
                v-model:visible="dataPickervisible"
              >
                <template #reference>
                  <el-button
                    type="success"
                    @click="getLessonInfo()"
                    style="margin-right: 1vw"
                    >课程日程</el-button
                  >
                </template>
                <el-table :data="lessonList" height="40vh">
                  <el-table-column
                    width="100"
                    property="sname"
                    label="学生姓名"
                  ></el-table-column>
                  <el-table-column width="300" label="开课时间">
                    <template #default="scope">
                      <el-date-picker
                        class="el-chapter-add-time"
                        v-model="scope.row.classTime"
                        type="datetime"
                        placeholder="选择日期时间"
                      >
                      </el-date-picker>
                    </template>
                  </el-table-column>
                  <el-table-column width="150" label="操作">
                    <template #default="scope">
                      <el-button
                        type="primary"
                        @click="submitLessonInfo(scope.$index, scope.row)"
                        >{{
                          islessonTime[scope.$index] ? "提交修改" : "创建日程"
                        }}</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </el-popover>

              <el-button
                type="info"
                @click="
                  addOrEditChapterVisible = false;
                  dataPickervisible = false;
                "
                >取消</el-button
              >
              <el-button
                type="primary"
                @click="addOrEditChapter('ChapterDataForm')"
                v-loading="addOrEditLoading"
                >{{
                  isAddChapter
                    ? addOrEditLoading
                      ? "正在提交"
                      : "添加"
                    : addOrEditLoading
                    ? "正在提交"
                    : "修改"
                }}</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-dialog>
      <el-menu
        v-loading="loading"
        class="manager-menu-style"
        text-color="rgb(30,30,30)"
      >
        <el-menu-item class="manager-menu-add-course" @click="addCourseInfo()">
          <h1 style="font-size: 2em; margin: auto">添加新的课程</h1>
        </el-menu-item>
        <el-menu-item
          class="manager-menu-item-course"
          v-for="(item, i) in courseList"
          :key="i"
          :index="i + ''"
          @click="selectCourse(i)"
        >
          <div>
            <div class="photo" :class="courseList_photo_color[i % 7]">
              <p class="photo-font">{{ courseList[i].name.substring(0, 1) }}</p>
            </div>
          </div>
          <div>
            <span class="course-name" slot="title"
              >{{ courseList[i].name }}
              {{ "curCount" in courseList[i] ? courseList[i].curCount : 0 }}/{{
                courseList[i].subCount
              }}</span
            >
            <span class="course-typeName" slot="title">{{
              courseList[i].typeName
            }}</span>
            <span class="course-description" slot="title">{{
              courseList[i].description
            }}</span>
            <div class="course-menubtn-group">
              <el-button
                class="course-menubtn-item"
                type="info"
                icon="el-icon-full-screen"
                @click="toCourseChapter(i)"
                circle
              ></el-button>
              <el-button
                class="course-menubtn-item"
                type="info"
                icon="el-icon-edit"
                @click="editCourseInfo(i)"
                circle
              ></el-button>
              <el-button
                class="course-menubtn-item"
                type="info"
                icon="el-icon-delete"
                @click="cancleCourse()"
                circle
              ></el-button>
            </div>
          </div>
        </el-menu-item>
      </el-menu>
    </div>
    <div
      class="chapter-manager"
      v-loading="chapterLoading"
      v-show="chapterManagerShow"
    >
      <span class="chapter-back-item" @click="backToCourseList()">⬅返 回</span>
      <h2 class="chapter-course-title">{{ curCourseName }}</h2>
      <a class="chapter-add-root-item" @click="appendTreeNodeRoot()">
        添加根章节
      </a>
      <el-tree
        v-loading="chapterTreeLoading"
        class="chapter-tree"
        :data="chapterTree"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node" v-popover:custom-tree-node-item>
            <span>{{ node.data.title }}</span>
            <span style="align-items: right !" v-show="!data.last">
              <!-- @click="appendTreeNode(data)" -->
              <a @click="addChapter(node, data)"> 添加 </a>
              <a @click="editChapter(node, data)"> 编辑 </a>
              <a @click="removeTreeNode(node, data)"> 删除 </a>
              <el-popover
                popper-class="chapter-detail"
                placement="top"
                :width="400"
                show-after="1"
                hide-after="1"
                trigger="hover"
                ref="custom-tree-node-item"
              >
                <template class="chapter-tree-node"> </template>
                <!-- <span v-if="node.isLeaf">
                  {{
                    node.data.classTime == null
                      ? "此章节暂未设置开课时间"
                      : "开课时间："
                  }}{{ node.data.classTime }}
                </span>
                <br v-if="node.isLeaf" /> -->
                <span>
                  章节详情：{{
                    data.description == "" ||
                    typeof data.description == "undefined"
                      ? "暂无详情"
                      : data.description
                  }}
                </span>
              </el-popover>
            </span>
          </span>
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script>
let id = 1000;
export default {
  name: "App",
  data() {
    return {
      editdialogVisible: false,
      courseList: [],
      courseList_photo_color: [],
      typeList: [],
      lessonList: [],
      islessonTime: [],

      courseInfo: {
        typeId: 1,
        courseTitle: "",
        courseDescription: "",
        subCount: 1,
        url: "",
      },
      courseInfoRules: {
        courseTitle: [{ required: true, message: "请输入课程标题" }],
        courseDescription: [{ required: true, message: "请输入课程描述" }],
      },
      ChapterData: {
        title: "",
        description: "",
      },
      ChapterDataRules: {
        title: [{required: true, message: "请输入章节标题"}]
      },

      curCourseId: -1,
      curcourseIndex: -1,
      curCourseName: "",
      curTypeName: "",
      curChapterNode: null,
      curChapterId: -1,
      curChapterNodeData: null,
      curCount: 0,
      tid: -1,
      curSubCount: 0,

      isAddChapter: true,
      isAddCourse: true,

      loading: true,
      submitCourseInfoLoading: false,
      courseLoading: true,
      chapterLoading: true,
      chapterTreeLoading: true,
      addOrEditLoading: false,

      courseManagerShow: true,
      chapterManagerShow: false,

      addOrEditChapterVisible: false,
      lessonVisible: false,
      editChapterTime: false,
      dataPickervisible: false,

      chapterTree: [],
    };
  },
  mounted() {
    var self = this;
    // let typeId = window.location.href.split("=")[1];
    // self.$api.CourseListByTypeId.url += self.typeId;
    self.courseList_photo_color = self.$photoColor.colorList;
    self.tid = self.$store.state.id;
    self.changeCourse();
    self.loading = false;
    self.courseLoading = false;

    self.$http.get(self.$api.CategoryList).then((res) => {
      self.typeList = JSON.parse(res.data.data);
    });
    console.log("typeList" + self.typeList);
  },
  methods: {
    changeCourse() {
      var self = this;
      self.typeId = window.location.href.split("=")[1];
      var url = self.$api.CourseListByTid.url;
      self.$api.CourseListByTid.url = url.split("=")[0] + "=" + self.tid;
      self.$http.get(self.$api.CourseListByTid).then((res) => {
        var courseData = JSON.parse(res.data.data);
        self.courseList = courseData;
        console.log(self.courseList);
      });
    },
    selectCourse(i) {
      var self = this;
      self.curCourseId = self.courseList[i].id;
      self.curcourseIndex = i;
    },
    toCourseChapter(i) {
      var self = this;
      self.chapterTree = [];
      self.chapterTreeLoading = true;
      self.curCourseName = self.courseList[i].name;
      self.curcourseIndex = i;
      self.curCourseId = self.courseList[i].id;
      self.courseManagerShow = false;
      self.chapterManagerShow = true;
      self.chapterSelect(self.curCourseId);
    },
    backToCourseList() {
      var self = this;
      self.courseManagerShow = true;
      self.chapterManagerShow = false;
    },
    addCourseInfo() {
      var self = this;
      self.editdialogVisible = true;

      self.courseInfo.typeId = 1;

      self.isAddCourse = true;
      self.courseInfo.courseTitle = "";
      self.courseInfo.courseDescription = "";
      self.courseInfo.subCount = 1;
    },
    editCourseInfo(i) {
      var self = this;
      self.editdialogVisible = true;
      self.isAddCourse = false;

      self.courseInfo.typeId = self.courseList[i].typeId;
      self.courseInfo.courseTitle = self.courseList[i].name;
      self.courseInfo.courseDescription = self.courseList[i].description;
      self.courseInfo.subCount = self.courseList[i].subCount;
      self.curCount = self.courseList[i].curCount;

      self.curcourseIndex = i;
      self.curSubCount = self.courseList[i].subCount;
    },
    cancleCourse() {
      var self = this;
      self
        .$confirm("此操作将删除该课程, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          var url = self.$api.CourseDelete.url;
          self.$api.CourseDelete.url =
            url.split("=")[0] + "=" + self.curCourseId;
          self.$http.get(self.$api.CourseDelete).then((res) => {
            if (res.data.success) {
              self.$message.success("删除成功!");
              self.courseList.splice(self.curcourseIndex, 1);
            } else if (
              res.data.statusCode ==
              self.$resultCode.COURSE_ALREADY_SELECTED.code
            ) {
              self.$message.warning(
                "删除失败，" + self.$resultCode.COURSE_ALREADY_SELECTED.message
              );
            } else {
              self.$message.error("删除失败，未知错误");
            }
          });
        })
        .catch(() => {
          self.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    closeCourseInfo(courseInfoForm) {
      var self = this;
      self.editdialogVisible = false;
      this.$refs[courseInfoForm].resetFields();
    },
    submitCourseInfo(courseInfoForm) {
      var self = this;
      if (
        self.courseInfo.courseTitle === "" ||
        self.courseInfo.courseDescription === ""
      ) {
        self.$message.warning("课程名和描述不得为空！");
        return;
      }
      self.submitCourseInfoLoading = true;
      if (self.isAddCourse) {
        let courseAddDTO = {
          tid: self.$store.state.id,
          typeId: self.courseInfo.typeId,
          name: self.courseInfo.courseTitle,
          description: self.courseInfo.courseDescription,
          subCount: self.courseInfo.subCount,
          url: self.courseInfo.url,
        };
        self.$http.post(self.$api.CourseInfoAdd, courseAddDTO).then((res) => {
          if (res.data.success) {
            self.$message.success("添加成功！");
            let courseLast = {
              id: res.data.data,
              typeId: self.courseInfo.typeId,
              typeName:
                self.typeList[
                  self.typeList.findIndex(
                    (t) => t.id === self.courseInfo.typeId
                  )
                ].title,
              name: self.courseInfo.courseTitle,
              description: self.courseInfo.courseDescription,
              subCount: self.courseInfo.subCount,
              url: self.courseInfo.url,
            };
            self.courseList.push(courseLast);
            self.submitCourseInfoLoading = false;
            self.closeCourseInfo(courseInfoForm);
          } else if (
            res.data.statusCode == self.$resultCode.DATABASE_INSERT_FAIL.code
          ) {
            self.$message.error(self.$resultCode.DATABASE_INSERT_FAIL.message);
            self.submitCourseInfoLoading = false;
            self.closeCourseInfo(courseInfoForm);
          }
        });
      } else {
        let courseInfoDTO = {
          id: self.curCourseId,
          typeId: self.courseInfo.typeId,
          name: self.courseInfo.courseTitle,
          description: self.courseInfo.courseDescription,
          subCount: self.courseInfo.subCount,
        };
        self.$http
          .post(self.$api.CourseInfoModify, courseInfoDTO)
          .then((res) => {
            if (res.data.data == -1) {
              self.$message.success("修改成功！");
              console.log(self.courseInfo.courseTitle);
              self.courseList[self.curcourseIndex].name =
                self.courseInfo.courseTitle;
              self.courseList[self.curcourseIndex].description =
                self.courseInfo.courseDescription;
              self.courseList[self.curcourseIndex].subCount =
                self.courseInfo.subCount;
              self.courseList[self.curcourseIndex].typeId =
                self.courseInfo.typeId;
              self.courseList[self.curcourseIndex].typeName =
                self.typeList[self.courseInfo.typeId - 1].title;
              self.submitCourseInfoLoading = false;
              self.closeCourseInfo(courseInfoForm);
            } else if (
              res.data.statusCode == self.$resultCode.DATABASE_UPDATE_FAIL.code
            ) {
              self.$message.error(
                self.$resultCode.DATABASE_UPDATE_FAIL.message + "!"
              );
            } else {
              self.$message.warning(
                "当前实际订阅课程人数为" +
                  res.data.data +
                  ",请重新确定课程容量！"
              );
              self.courseList[self.curcourseIndex].curCount = res.data.data;
              self.curCount = res.data.data;
            }
            self.submitCourseInfoLoading = false;
          });
      }
    },
    getLessonInfo() {
      var self = this;
      var url = self.$api.LessonInfo.url;
      if (self.dataPickervisible) {
        self.islessonTime = self.islessonTime.splice(
          0,
          self.islessonTime.length
        );
        self.dataPickervisible = false;
        return;
      }
      self.$api.LessonInfo.url = url.split("=")[0] + "=" + self.curChapterId;
      self.$http.get(self.$api.LessonInfo).then((res) => {
        self.lessonList = res.data.data;
        for (var i = 0; i < self.lessonList.length; i++) {
          self.islessonTime.push(self.lessonList[i].classTime != null);
        }
        console.log(res.data.data);
      });
      self.dataPickervisible = true;
    },

    submitLessonInfo(index, row) {
      var self = this;

      if (self.islessonTime[index]) {
        self
          .$prompt("您将请求修改该课程的开课时间, 请说明原因?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            inputPattern: /\S/,
            inputErrorMessage: "原因不能为空",
          })
          .then(({ value }) => {
            let LessonInfoDTO = {
              cid: self.curCourseId,
              chid: self.curChapterId,
              tid: self.$store.state.id,
              sid: row.sid,
              teacher: true,
              content: value,
              classTime: row.classTime,
            };

            console.log(self.lessonList[index].classTime);
            if (row.classTime == null) {
              self.$message.warning("请选择开课时间！");
            } else {
              self.$http
                .post(self.$api.ScheduleLessonSet, LessonInfoDTO)
                .then((res) => {
                  if (res.data.success) {
                    self.$message.success(res.data.message);
                  } else {
                    self.$message.error(res.data.message);
                  }
                });
            }
          })
          .catch(() => {
            self.$message({
              type: "info",
              message: "已取消处理",
            });
            return;
          });
      } else {
        let LessonInfoDTO = {
          cid: self.curCourseId,
          chid: self.curChapterId,
          tid: self.$store.state.id,
          sid: row.sid,
          teacher: true,
          content: "",
          classTime: row.classTime,
        };

        if (row.classTime == null) {
          self.$message.warning("请选择开课时间！");
        } else {
          self.$http
            .post(self.$api.ScheduleLessonSet, LessonInfoDTO)
            .then((res) => {
              if (res.data.success) {
                self.$message.success(res.data.message);
                self.islessonTime[index] = true;
              } else {
                self.$message.error(res.data.message);
              }
            });
        }
      }
    },

    chapterSelect(cid) {
      var self = this;
      var url = self.$api.ChapterSelect.url;
      self.$api.ChapterSelect.url = url.split("=")[0] + "=" + cid;
      self.$http
        .get(self.$api.ChapterSelect)
        .then((res) => {
          if (res.data.data != "") {
            self.chapterTree = JSON.parse(res.data.data);
          } else {
            self.chapterTree = [];
          }
          self.chapterTree.push({ last: true });
          self.chapterTree.push({ last: true });
          self.chapterTree = [...self.chapterTree];
          self.chapterTreeLoading = false;
        })
        .catch((e) => {
          self.$message({
            type: "info",
            message: e,
          });
        });
    },
    addChapter(node, data) {
      var self = this;
      self.curChapterNodeData = data;
      self.curChapterNode = node;

      if (node.isLeaf && data.classTime != null) {
        self.$message.error("此章节已设置具体的开课时间，不可再向下扩展！");
        return;
      }

      self.ChapterData.title = "";
      self.ChapterData.description = "";

      self.editChapterTime = false;
      self.addOrEditChapterVisible = true;
      self.isAddChapter = true;
    },
    editChapter(node, data) {
      var self = this;

      self.curChapterNodeData = data;
      self.curChapterNode = node;
      self.curChapterId = data.id;

      self.ChapterData.title = data.title;
      self.ChapterData.description = data.description;
      self.editChapterTime = node.isLeaf;

      self.addOrEditChapterVisible = true;
      self.isAddChapter = false;
    },
    addOrEditChapter(ChapterDataForm) {
      var self = this;

      self.$refs[ChapterDataForm].validate((valid) => {
        if (valid) {
          self.addOrEditLoading = true;
          if (self.isAddChapter) {
            self.appendTreeNode(self.curChapterNodeData);
            self.addOrEditChapterVisible = false;
          } else {
            self.editTreeNode(self.curChapterNode, self.curChapterNodeData);
            self.addOrEditChapterVisible = false;
          }
          self.addOrEditLoading = false;
        } else {
          self.$message.warning("请输入信息！");
        }
      });
    },
    appendTreeNodeRoot() {
      var self = this;
      // console.log(self.chapterTree);
      // self.chapterTree.pop();
      // self.chapterTree.pop();
      // self.chapterTree = [...self.chapterTree];
      // console.log(self.chapterTree);
      self
        .$prompt("请输入章节名称", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /^[s|S]{1,}$/g,
          inputErrorMessage: "请输入章节名",
        })
        .then(({ value }) => {
          var title = value;
          var len = self.chapterTree.length;
          console.log(self.chapterTree.length);
          let chapterRootDTO = {
            cid: self.curCourseId,
            preChid: len > 2 ? self.chapterTree[len - 3].id : 0,
            title: title,
            description: "",
            url: "",
          };
          self.$http
            .post(self.$api.ChapterInsertRoot, chapterRootDTO)
            .then((res) => {
              var chapterVO = JSON.parse(res.data.data);
              self.$message.success("添加的章节标题: " + value);
              // const root = { id: chapterVO.id, title: title, chridren: [] };
              // self.chapterTree.push(chapterVO);
              self.chapterTree.splice(
                self.chapterTree.length - 2,
                0,
                chapterVO
              );
              self.addChapterVisible = false;
              // self.chapterTree = [...self.chapterTree];
              console.log(self.chapterTree);
            });
        })
        .catch((e) => {
          self.$message({
            type: "info",
            message: "取消输入",
          });
        });
      // self.chapterTree.push({ last: true });
      // self.chapterTree.push({ last: true });
    },
    appendTreeNode(data) {
      var self = this;

      // self
      //   .$prompt("请输入章节名称", "提示", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     // inputPattern: ,
      //     // inputErrorMessage: '邮箱格式不正确'
      //   })
      //   .then(({ value }) => {
      // var title = value;
      console.log(data.children);
      console.log(data.children == null);
      console.log(typeof data.children);
      let chapterNodeDTO = {
        cid: self.curCourseId,
        preChid:
          typeof data.children != "undefined" && data.children.length != 0
            ? data.children[data.children.length - 1].id
            : 0,
        parChid:
          typeof data.children != "undefined" && data.children.length != 0
            ? 0
            : data.id,
        title: self.ChapterData.title,
        description: self.ChapterData.description,
        url: "",
      };
      console.log(
        "parChid==" +
          chapterNodeDTO.parChid +
          ";preChid==" +
          chapterNodeDTO.preChid
      );
      self.$message.success("添加的章节标题: " + self.ChapterData.title);
      this.$http
        .post(this.$api.ChapterInsertNode, chapterNodeDTO)
        .then((res) => {
          var chapterVO = JSON.parse(res.data.data);
          // const newChild = {id: res.data.data,title: title,children: [],};
          if (!data.children) {
            data.children = [];
          }
          data.children.push(chapterVO);
          self.chapterTree = [...self.chapterTree];
        });
      // })
      // .catch((e) => {
      //   self.$message({
      //     type: "info",
      //     message: "取消输入" + e,
      //   });
      // });
    },
    editTreeNode(node, data) {
      var self = this;
      let chapterNodeDTO = {
        id: data.id,
        cid: self.curCourseId,
        preChid: self.curChapterNodeData.preChid,
        parChid: self.curChapterNodeData.parChid,
        title: self.ChapterData.title,
        description: self.ChapterData.description,
        url: "",
      };
      self.$http
        .post(self.$api.ChapterUpdateNode, chapterNodeDTO)
        .then((res) => {
          if (res.data.data == 1) {
            self.$message.success("修改成功！");
            data.title = chapterNodeDTO.title;
            data.description = chapterNodeDTO.description;
            data.url = chapterNodeDTO.url;
          } else {
            self.$message.error("修改失败！");
          }
        });
    },
    removeTreeNode(node, data) {
      var self = this;
      self.chapterTree.pop();
      self.chapterTree.pop();
      self
        .$confirm("此操作将删除该章节, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          const parent = node.parent;
          const children = parent.data.children || parent.data;
          const index = children.findIndex((d) => d.id === data.id);
          var chidList = [],
            childrenIndex = [...children[index].children];
          chidList.push(children[index].id);
          while (childrenIndex.length > 0) {
            var item = childrenIndex.shift();
            chidList.push(item.id);
            if (item.children.length != 0) {
              for (var j = 0; j < item.children.length; j++) {
                childrenIndex.push(item.children[j]);
              }
            }
          }
          console.log(chidList);

          let chapterNoteDeleteDTO = {
            ids: chidList,
            preChid: index > 0 ? children[index - 1].id : 0,
            parChid: index > 0 ? 0 : parent.data.id,
          };
          console.log(chapterNoteDeleteDTO);
          self.$http
            .post(self.$api.ChapterDeleteNode, chapterNoteDeleteDTO)
            .then((res) => {
              this.$message.success("已删除" + res.data.data + "条数据");
            });
          children.splice(index, 1);
          self.chapterTree.push({ last: true });
          self.chapterTree.push({ last: true });
          self.chapterTree = [...self.chapterTree];
          self.$message({ type: "success", message: "删除成功!" });
        })
        .catch((e) => {
          this.$message({
            type: "info",
            message: "已取消删除" + e,
          });
        });
    },
  },
  watch: {
    $route() {
      var self = this;

      // self.reFresh = true;
    },
  },
};
</script>

<style>
.manager-menu-style {
  background: transparent;
  color: black;
  width: 54vw !important;
  background: transparent !important;
  border: none !important;
}
.manager-menu-item-course {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 12vh !important;
  width: 60vw;
  line-height: 6vh !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.manager-menu-add-course {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 12vh !important;
  width: 60vw !important;
  line-height: 6vh !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
/* 鼠标覆盖的样式 */
.manager-menu-item-course:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: black !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}

/* 被选中的样式 */
.manager-menu-item-course.is-active {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.75) !important;
  font-weight: bold;
  color: white !important;
}

.photo {
  height: 4em;
  width: 4em;
  border-radius: 50%;
  background: red;
  display: flex;
  justify-content: center;
  align-items: center;
}
.photo-font {
  padding-top: 0 !important;
  font-size: 3em;
  color: white !important;
}
.course-name {
  position: absolute;
  left: 6em;
  font-weight: bold;
  height: 4vh;
}
.course-typeName {
  position: absolute;
  right: 1vw;
  font-weight: bold;
  height: 4vh;
}
.course-description {
  position: absolute;
  text-align: left;
  top: 4vh;
  left: 6em;
  height: 8vh;
  line-height: 4vh;
  width: 30vw;
  white-space: pre-wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.course-menubtn-group {
  position: absolute;
  right: 4vw;
  top: 4vh;
  display: flex;
}
.course-menubtn-item {
  width: 3em;
  height: 3em;
  color: white !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.courseInfo-description {
  max-height: 40vh !important;
}

.chapter-manager {
  border-radius: 15px;
  width: 60vw;
  height: 74vh;
  background: rgb(238, 238, 238);
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.chapter-back-item {
  cursor: pointer;
  color: gray;
  position: absolute;
  top: 2vh;
  left: 2vw;
}
.chapter-course-title {
  padding-top: 2vh;
  color: rgb(90, 90, 90);
}
.chapter-add-root-item {
  border-radius: 10px;
  cursor: pointer;
  position: absolute;
  padding: 1vh 1vw 1vh 1vw;
  background: rgb(86, 86, 86);
  top: 2vh;
  right: 5.5vw;
  color: #fff;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.chapter-tree {
  border-radius: 10px;
  margin-top: 2vh;
  margin-left: 2vw;
  width: 56vw;
  height: 62vh;
  background: rgb(86, 86, 86) !important;
  box-shadow: -8px -8px 16px -10px rgb(238, 238, 238),
    8px 8px 16px -10px #000 inset;
  overflow: scroll;
}
.chapter-tree::-webkit-scrollbar {
  width: 0 !important;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  color: white;
}
.chapter-detail {
  width: 16vw !important;
  display: flex;
  flex-direction: column;
}
.chapter-tree-node-item {
  height: 6vh !important;
}
.el-chapter-add-time {
  margin-right: 2vh;
  width: 240px !important;
}
</style>