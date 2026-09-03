<template>
  <div>
    <el-menu>
      <el-menu-item
        class="note-menu-item"
        v-for="(item, i) in noteList"
        :key="i"
        :index="i + ''"
        @click=""
      >
        <div style="position: absolute; left: 1vw">
          {{ noteList[i].createTime.split("T")[0] }}
        </div>
        <div style="position: absolute; left: 1vw; top: 3vh">
          {{ noteList[i].createTime.split("T")[1] }}
        </div>
        <el-button
          class="note-edit-btn"
          type="info"
          @click="editNote(i)"
          icon="el-icon-edit"
        ></el-button>
        <el-button
          class="note-delete-btn"
          type="info"
          @click="deleteNote(i)"
          icon="el-icon-delete"
        ></el-button>
        <div
          class="ql-editor content-detail content-note"
          v-html="noteList[i].content"
        ></div>
      </el-menu-item>
    </el-menu>
    <el-dialog
      class="note-dialog"
      title="随堂笔记"
      :visible.sync="noteDialogVisible"
      :show-close="false"
    >
      <el-form>
        <el-form-item prop="noteContent">
          <quill-editor
            class="editor my-editor"
            v-model="noteContent"
            ref="myQuillEditor"
            :options="editorOption"
            @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)"
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
  </div>
</template>

<script scoped>
export default {
  name: "App",
  data() {
    return {
      noteList: [],
      noteContent: "",
      editorOption: {},

      curNoteId: -1,
      curNoteIndex: -1,

      noteDialogVisible: false,
    };
  },
  mounted() {
    var self = this;
    var url = self.$api.SelectNoteList.url;
    self.$api.SelectNoteList.url =
      url.split("=")[0] + "=" + self.$store.state.id;
    self.$http.get(self.$api.SelectNoteList).then((res) => {
      self.noteList = JSON.parse(res.data.data);
    });
  },
  methods: {
    editNote(i) {
      var self = this;
      self.curNoteId = self.noteList[i].id;
      self.curNoteIndex = i;

      self.noteContent = self.noteList[i].content;
      self.noteDialogVisible = true;
    },
    submitNote() {
      var self = this;
      let NoteModifyDTO = {
        id: self.curNoteId,
        sid: self.$store.state.id,
        content: self.noteContent,
      };
      self.$http.post(self.$api.ModifyNote, NoteModifyDTO).then((res) => {
        if (res.data.success) {
          self.noteList[self.curNoteIndex].content = self.noteContent;
          self.$message.success("修改成功！");
          self.noteDialogVisible = false;
        } else {
          self.$message.error("修改失败，未知错误！");
        }
      });
    },
    deleteNote(i) {
      var self = this;
      self
        .$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          var url = self.$api.DeleteNote.url;
          self.$api.DeleteNote.url =
            url.split("=")[0] + "=" + self.noteList[i].id;
          self.$http.get(self.$api.DeleteNote).then((res) => {
            if (res.data.success) {
              self.noteList.splice(i, 1);
              self.$message({
                type: "success",
                message: "删除成功!",
              });
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
    onEditorBlur() {},
    onEditorFocus() {
      //获得焦点事件
    },
    onEditorChange() {
      //内容改变事件
    },
  },
};
</script>

<style>
.content-note {
  position: relative;
  margin-top: 1vh;
  margin-bottom: 1vh;
  left: 5vw;
  width: 50vw;
  height: 14vh;
}
.note-menu-item {
  display: flex;
  border-radius: 5px;
  margin-top: 1vh;
  margin-bottom: 1vh;
  font-size: 12px;
  background: rgb(238, 238, 238);
  height: 16vh !important;
  width: 60vw;
  line-height: 6vh !important;
  color: rgb(55, 55, 55) !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.note-menu-item:hover {
  cursor: pointer;
  background-color: rgba(150, 150, 150, 0.35) !important;
  color: black !important;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000 inset;
}
.note-edit-btn {
  border-radius: 12px;
  padding: 0px 0px 2px 9px !important;
  width: 36px;
  height: 36px;
  position: absolute;
  left: 15px;
  top: 8vh;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.note-delete-btn {
  border-radius: 12px;
  padding: 0px 0px 2px 9px !important;
  width: 36px;
  height: 36px;
  position: absolute;
  left: 45px;
  top: 8vh;
  box-shadow: -8px -8px 16px -10px #fff, 8px 8px 16px -10px #000;
}
.my-editor {
  width: 90vw;
  height: 40vh;
  margin-bottom: 2vh;
}
.note-dialog .el-dialog {
  width: 94vw !important;
  height: auto;
}
</style>