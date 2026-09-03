/**
 * api.js 用于存放后端所有的接口地址
 */
 export default {
    /**
     * 用户登录
     * POST Login/SignIn
     */
    Login: {
      url: 'Login/SignIn'
    },
    LoginAdmin: {
      url: '/Login/Admin/Login?pwd='
    },
    SignUp: {
      url: 'Login/SignUp'
    },
    CategoryList: {
      url: '/Course/Category/List'
    },
    CourseList: {
      url: '/Course/Course/List'
    },
    CourseListByTypeId: {
      url: '/Course/Course/ListByType?typeId='
    },
    CourseListByTid: {
      url: '/Course/Course/ListByTid?tid='
    },
    CourseListBySid: {
      url: '/Course/Course/ListBySid?sid='
    },
    CourseListBySearch: {
      url: '/Course/Course/List/Search?content='
    },
    CourseDetails: {
      url: '/Course/Course/Details?courseId='
    },
    SubscribeCourse: {
      url: '/Course/Course/Subscribe?courseId='
    },
    TeacherInfo: {
      url: '/User/Teacher/Info?tid='
    },
    StudentInfo: {
      url: '/User/Student/Info?sid='
    },
    UserInfoModify: {
      url: '/User/User/ModifyInfo'
    },
    AddNote: {
      url: 'User/User/AddNote'
    },
    ModifyNote: {
      url: 'User/User/ModifyNote'
    },
    DeleteNote: {
      url: 'User/User/DeleteNote?id='
    },
    SelectNoteList: {
      url: 'User/User/ListNode?sid='
    },
    CourseInfoModify: {
      url: '/Course/Course/ModifyInfo'
    },
    CourseInfoAdd: {
      url: '/Course/Course/Add'
    },
    CourseDelete: {
      url: '/Course/Course/Delete?cid='
    },
    CourseUnsubscribe: {
      url: '/Course/Course/Unsubcribe'
    },
    LessonInfo: {
      url: '/Course/Chapter/LessonInfo?chid='
    },
    ChapterSelect: {
      url: '/Course/Chapter/Select?cid='
    },
    ChapterInsertRoot: {
      url: '/Course/Chapter/Insert/Root'
    },
    ChapterInsertNode: {
      url: '/Course/Chapter/Insert/Node'
    },
    ChapterDeleteNode: {
      url: '/Course/Chapter/Delete'
    },
    ChapterUpdateNode: {
      url: '/Course/Chapter/Update/Node'
    },
    CalendarAllTeacher: {
      url: '/Schedule/Calendar/All/Teacher?tid='
    },
    CalendarAllStudent: {
      url: '/Schedule/Calendar/All/Student?sid='
    },
    ScheduleDayTeacher: {
      url: '/Schedule/Schedule/Day/Teacher'
    },
    ScheduleDayStudent: {
      url: '/Schedule/Schedule/Day/Student'
    },
    ScheduleTodayClassCount: {
      url: '/Schedule/Schedule/Today/ClassCount?id='
    },
    ScheduleLessonSet: {
      url: '/Schedule/Schedule/Lesson/Set'
    },

    RequestListByTid: {
      url: '/Request/Request/ListByTid?tid='
    },
    RequestListBySid: {
      url: '/Request/Request/ListBySid?sid='
    },
    RequestCountByTid: {
      url: '/Request/Request/CountByTid?tid='
    },
    RequestCountBySid: {
      url: '/Request/Request/CountBySid?sid='
    },
    RequestSolve: {
      url: '/Request/Request/Solve'
    },

    AdminTeacherList: {
      url: '/Admin/User/Teacher'
    },
    AdminStudentList: {
      url: '/Admin/User/Student'
    },
    AdminChangeStatus: {
      url: '/Admin/User/Status?id='
    },
    AdminChangeCourseStatus: {
      url: '/Admin/Course/Status?id='
    },
    AdminCategoryToCourseList: {
      url: '/Admin/Course/Category?id='
    },
    AdminTeacherToCourseList: {
      url: '/Admin/Course/Teacher?id='
    },

    TianAPIWord: {
      url: '/txapi/enwords/index?key=8c89da33c0ca5eaf95ec25e1abc32dd6&word='
    }
}