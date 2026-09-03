import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login/Login.vue'
import store from '@/store'
// import CourseDetail from '@/views/Course/CourseDetail.vue'
import CenterTeacher from '@/views/Center/Teacher.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/course/category',
    name: 'Category',
    component: Home
  },
  {
    path: '/course/search',
    name: 'Search',
    component: Home
  },
  {
    path: '/course/details',
    name: 'CourseDetails',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/room',
    name: 'Room',
    component: () => import('../views/Room.vue')
  },{
    path: '/room/teacher',
    name: 'TeacherRoom',
    component: () => import('../views/Room/Teacher.vue')
  },{
    path: '/room/student',
    name: 'StudentRoom',
    component: () => import('../views/Room/Student/PC.vue')
  },{
    path: '/room/student/mobile',
    name: 'StudentRoomMobile',
    component: () => import('../views/Room/Student/Mobile.vue')
  },{
    path: '/center/teacher',
    name: 'CenterTeacher',
    component: CenterTeacher
  }
  ,{
    path: '/center/student',
    name: 'CenterStudent',
    component: () => import('../views/Center/Student.vue')
  },
  ,{
    path: '/center/admin',
    name: 'CenterAdmin',
    component: () => import('../views/Center/Admin.vue')
  },
  {
    path: '/admin/login',
    name: 'LoginAdmin',
    component: () => import('../views/Login/LoginAdmin.vue')
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})
/**
 * 设置路由全局守卫
 */
router.beforeEach((to,from,next) => {
  if(Vue.cookie.get('id') != null){

    //已经登录
    if ((to.name === 'StudentRoom' || to.name === 'CenterStudent') && Vue.cookie.get('roleId') == 1){
        alert("教师不能进入学生客户端哦！");
        next(
          {
            name: to.name === 'StudentRoom'?'TeacherRoom':'CenterTeacher'
          }
        )
    }else if((to.name === 'TeacherRoom' || to.name === 'CenterTeacher') && Vue.cookie.get('roleId') == 2){
        alert("学生不能进入教师客户端哦！");
        next(
          {
            name: to.name === 'TeacherRoom'?'StudentRoom':'CenterStudent'
          }
        )
    }

    if(to.name === 'Login'){

      let toPath = ''

      switch (Vue.cookie.get('roleId')) {
        // switch (store.state.roleName) {
        case 0:
          toPath = '/admin'
          break;
        case 1:
          toPath = '/room/teacher'
          break;
        case 2:
          toPath = '/room/student'
          break;
        default:
          console.log(store.state.roleName + "身份未知");
      }
      next(toPath)
    }else{
      next()
    }
  }else{
    //未登录
    if (to.name === 'Login' || to.name === 'Home' || to.name === 'Category' || to.name === 'LoginAdmin' ||
    to.name === 'CourseDetails' || to.name === 'Search' ){//未登录时可以开放的路由
      next();
    } else {
      // next() //临时

      alert("用户未登录或登录已超时!");

      next(
        {
          name: 'Login'
          // query: { redirect: to.fullPath }
        }
      )
    }
  }
});

export default router
