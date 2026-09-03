# Spark Live Classroom(一对一英语直播教学平台)

基于 **Spring Boot + Vue** 的在线英语一对一直播教学平台,围绕「教师开设课程 → 学生订阅约课 → 按章节排课 → WebRTC 直播间授课 → 调课/退订请求流转」的完整闭环设计,并提供管理员后台与基于角色的三端入口。

仓库为 2021 年前后的教学演示工程(代码集中于 2021-03 ~ 2021-06),前后端、数据库脚本与设计文档齐备,适合作为 Web 全栈 / 直播 / RBAC 课题的学习参考。

---

## 目录结构

```text
spark-live-classroom/
├── backend/                  # 后端:Spring Boot(Maven)单体工程
│   ├── pom.xml               # Spring Boot 2.4.4 / Java 8 / MyBatis-Plus / Druid
│   └── src/main/
│       ├── java/top/spark/live/
│       │   ├── controller/   # Login / User / Course / Schedule / Request / Admin
│       │   ├── service/      # 业务层 + impl
│       │   ├── mapper/       # MyBatis-Plus Mapper(13 张表)
│       │   ├── entity/ dto/ vo/   # 实体 / 入参 / 出参
│       │   ├── constant/     # 角色、请求类型/状态、排课/订阅返回码等常量
│       │   └── result/       # 统一返回 JsonResult / ResultCode
│       └── resources/application.yml
├── front/
│   ├── live-room/            # 前端:Vue 2 + Element UI 工程(当前主前端)
│   │   └── src/
│   │       ├── views/        # Home、Login、Center(Admin/Teacher/Student)、Room 等
│   │       ├── components/   # course/category/Admin/Schedule/Request/Note 等
│   │       ├── http/         # api.js / http.js / resultCode.js
│   │       ├── router/ store/ utils/ assets/
│   │       └── ...
│   └── live2021-04-21.zip    # 2021-04-21 前端源码打包快照
├── last/live-room/           # 早期纯静态原型:EasyUI + 原生 WebSocket + WebRTC
│                              # (teacher.html / student/ / qr.html / over.html)
├── sql/
│   ├── spark_live.sql        # 库表 DDL + 演示数据(13 张表)
│   ├── 无限分类数据库设计.md   # 分类 pid 邻接表递归成树方案
│   ├── RBAC.eddx / rbac.ndm2 # RBAC 权限模型图 / 早期库表建模
├── 需求分析/                  # 直播模块需求(直播模块v1.0.docx、交互/视频流图)
└── 系统设计/                  # 整体、请求模块、订阅(并发)、课程(日程/删除)、
                               # 直播(WebRTC 时序图、直播间表 PDM)等 xmind/建模文件
```

---

## 功能特性

| 模块 | 说明 |
|---|---|
| 多角色登录 | 管理员 / 教师 / 学生三端独立入口;学生、教师可自助注册;`tu_user_role_relation` 绑定角色 |
| 课程管理 | 教师 CRUD 课程(标题、简介、分类、订阅上限 1~20);管理员可上下架课程并广播「知会」 |
| 分类体系 | `tc_category` 按 **pid 邻接表实现无限分类**,首页按分类筛选课程 |
| 章节管理 | 课程章节以 **兄弟/孩子双指针(`nid`/`crid`)邻接树** 存储,支持任意层级、增删改并实时维护树指针 |
| 订阅约课 | 学生浏览/搜索 → 订阅课程;订阅数超 `sub_count` 拒绝(带并发上限保护);退订需发起申请由教师处理 |
| 排课 / 日程 | 教师为「学生 × 章节」排定开课时间(±30 分钟冲突检测,冲突则拒绝);月历按天聚合课次,当日课表可直达直播间 |
| 调课 / 退订流转 | 请求统一存 `tu_request`:取消订阅、变更开课时间、知会三类;通过/驳回后自动给发起方回执「知会」 |
| 在线直播 | WebRTC 一对一:教师端采集 **摄像头 + 屏幕共享** 双路推送,学生 PC 端接听,自研 WebSocket 信令 + 聊天(详见下文) |
| 随堂互动 | 学生直播间随堂笔记(富文本)、消息翻译(腾讯天行 API)、呼叫/语音/全屏请求 |
| 管理后台 | 教师/学生账户启停、按分类或按教师管理课程上下线 |
| 个人中心 | 师生资料维护;学生「我的课程/日程/笔记/请求」,教师「课程管理/日程/请求」 |

---

## 角色与主要操作

| 角色 | 主要页面 | 核心操作 |
|---|---|---|
| 管理员 | `/admin/login` → 个人中心 Admin | 启停教师/学生账号;按分类、按教师对课程上下线(上下线自动知会教师与订阅学生) |
| 教师 | 个人中心 Teacher | 课程 CRUD 与章节树维护;为每名学生排课/改期;月历日程;进入直播间授课(开媒体 → 呼叫学生);处理退订/改期请求 |
| 学生 | 个人中心 Student | 订阅课程、申请取消订阅;查看日程并进入直播间(接听视频/语音);富文本随堂笔记;处理收到的请求回执 |

> 注:后端接口以显式传参(`tid`/`sid`/`id`)区分身份,前端按角色路由并跳转,登录态为 20 分钟有效的 cookie(`id`/`userId`/`roleId`)。

---

## 技术栈

### 后端 `backend/`

| 组件 | 选型 |
|---|---|
| 框架 | Spring Boot 2.4.4(内嵌 Tomcat),Java 8,Maven |
| ORM | MyBatis-Plus 3.0.7.1 |
| 连接池 / 驱动 | Druid 1.1.10 / mysql-connector-java 6.0.6 |
| JSON | fastjson 1.2.62(部分接口 `data` 为二次序列化的 JSON 字符串) |
| 端口 | `8088`,Jackson 时区 GMT+8 |

### 前端 `front/live-room/`

| 组件 | 选型 |
|---|---|
| 框架 | Vue 2.6 + Vue CLI 4.5(webpack 4) |
| UI / 状态 / 路由 | Element UI 2.15 / Vuex 3 / Vue Router 3 |
| 请求 | axios(`baseURL=/api`,开发期由 devServer 代理到 `localhost:8088`) |
| 登录态 | vue-cookie(`id`/`userId`/`roleId`,20 分钟) |
| 富文本 / 直播 | vue-quill-editor(随堂笔记)/ WebRTC + 原生 WebSocket 信令(直播教室) |
| 其它 | webrtc-adapter、vue-aliplayer-v2(仅 `/room` 历史演示页) |

---

## 数据库

`sql/spark_live.sql` 提供完整 DDL 与演示数据,共 **13 张表**(InnoDB / utf8mb4),逻辑上分用户域(`tu_*`)与课程域(`tc_*`),无物理外键,全部为逻辑关联:

| 表 | 用途 |
|---|---|
| `tu_user` | 用户(登录名、邮箱、手机、启用/禁用) |
| `tu_role` | 角色:`0=admin` / `1=teacher` / `2=student` |
| `tu_user_role_relation` | 用户-角色关联(实际生效) |
| `tu_permissions` / `tu_role_permission_relation` | 权限点与角色-权限关联(**表结构已建,未启用**) |
| `tu_request` | 请求/消息:`rid`(1 取消订阅 / 2 变更开课时间 / 3 知会)、`direction`(1 教师→学生 / 2 学生→教师 / 3 管理员广播)、`status`(1 未处理 / 2 已处理)、`param_json` 业务参数 |
| `tu_note` | 学生笔记(富文本 HTML) |
| `tc_category` | 课程分类,pid 无限分类(小学/初中/高中/大学/职场/雅思/托福/考研…) |
| `tc_course` | 课程(教师 `tid`、分类 `type_id`、订阅上限 `sub_count`、状态) |
| `tc_chapter` | 章节,`nid`(下一兄弟)/ `crid`(首孩子)双指针组织任意层级树 |
| `tc_chapter_root_first` | 每门课程的首个根章节索引 |
| `tc_course_selection` | 学生订阅关系 |
| `tc_schedule` | 排课:`(cid, chid, sid)` 联合主键、`room_id` 直播间号、`class_time` 开课时间 |

---

## 快速开始

环境要求:JDK 8+、Maven、Node.js、MySQL 5.7+。

1. **初始化数据库**

   在 MySQL 中执行 `sql/spark_live.sql`(自动建库 `spark_live` 并写入演示数据,演示账号见文件内的初始化数据)。

2. **启动后端**

   ```bash
   cd backend
   mvn spring-boot:run        # 或直接运行 SparkLiveApplication
   ```

   默认连接 `jdbc:mysql://spark-young.top:3306/spark_live`,请按需修改 `src/main/resources/application.yml` 中的数据源地址、账号与密码;服务监听 `8088`。

3. **启动前端**

   ```bash
   cd front/live-room
   npm install
   npm run serve              # 开发服务器已将 /api 代理到 http://localhost:8088
   ```

   生产构建 `npm run build`(注意 `vue.config.js` 中 `outputDir` 与 `package.json` 的 `push` 脚本指向本机绝对路径 `X:\workspace\vue\push\live`,需按实际环境调整)。

---

## 直播模块

直播为 **网页端 WebRTC 一对一**(教师推流、学生收流),自研 WebSocket 信令,不依赖任何第三方 SDK:

- **教师端**(`views/Room/Teacher.vue`):采集摄像头 + 屏幕共享,建立两条 PeerConnection 通道(摄像头 / 屏幕),经 WS 交换 offer / answer / ICE,心跳保活与断线重连;
- **学生端**(`views/Room/Student/PC.vue`):收到呼叫弹窗 → 选择视频/语音 → 建立连接,支持随堂笔记、消息翻译、教师发起的全屏/刷新等指令;
- 直播间号由排课记录中的 `room_id` 携带,页面以 `?ijt=` 参数进入;
- 信令地址、STUN/TURN 服务器集中在 `src/assets/js/common.js`(`ICEServer`),演示指向 `spark-young.top` 与腾讯云 `119.29.61.66:3478` —— 自行部署时需替换为可用的信令服务与 TURN 服务;
- 仓库内另有两条直播路线参考资料:历史演示页 `/room`(Aliplayer FLV 拉流)与早期静态原型 `last/live-room`(EasyUI + 原生 WebSocket 信令的师生视频对讲)。

> 提醒:直播功能依赖独立的信令服务端与 TURN 服务,当前仓库未包含该服务端代码(仅前端接入与早期原型痕迹)。

---

## 接口概览

后端共 6 个 Controller,路径与模块对应:

| 前缀 | 模块 | 典型接口 |
|---|---|---|
| `/Login` | 认证 | SignIn(登录)、SignUp(注册)、Admin/Login(管理员口令登录) |
| `/User` | 个人中心 | Teacher/Student Info、ModifyInfo、AddNote/ModifyNote/DeleteNote/ListNode |
| `/Course` | 课程/分类/章节 | Course List/Details/Add/ModifyInfo/Delete/Subscribe/Unsubcribe、Category List、Chapter Select/Insert/Delete/Update |
| `/Schedule` | 排课/日程 | Calendar/All/Teacher·Student、Schedule/Day/Teacher·Student、Lesson/Set、Today/ClassCount |
| `/Request` | 请求流转 | List/CountByTid、List/CountBySid、Solve(通过/驳回) |
| `/Admin` | 后台管理 | User/Teacher·Student、User/Status、Course/Category·Teacher、Course/Status |

统一返回结构:`JsonResult { success, statusCode, message, data }`,状态码枚举见后端 `result/ResultCode.java` 与前端 `src/http/resultCode.js`。

---

## 设计要点与已知局限

**设计要点**

- 章节树、分类树均支持无限层级:分类用 pid 邻接表递归,章节用 `nid`/`crid` 双指针邻接表 + 课程根章节索引,后端拼装为树形 JSON;
- 订阅上限用实例级 `ReentrantLock` 做并发保护;排课冲突按 ±30 分钟时间窗检测;重复请求以「方向 + 参数 + 类型」去重;
- 请求流转为状态机:`tu_request` 上未处理 → 处理方通过/驳回 → 自动向发起方写入「知会」回执,发起方确认后清理;
- 管理端上下架课程会向该课教师与每位订阅学生广播「知会」请求。

**已知局限(接手前请注意)**

- 鉴权仅停留在「前端 cookie + 路由守卫」层面,后端接口不校验身份与角色;RBAC 的权限表(`tu_permissions` 等)仅建表未启用;
- 用户密码为明文存储;部分 SQL 使用 `${}` 字符串拼接,存在注入风险;
- 业务代码基本无事务控制,数据一致性主要靠调用顺序保证;
- 部分列表/详情接口 `data` 字段是 fastjson 二次序列化的 JSON 字符串,前端需再次 `JSON.parse`;
- 排课时间做 `plusHours(8)` 时区补偿,依赖后端 GMT+8 时区配置;
- 学生移动端页面、`/room` Aliplayer 页、`qr.html`、`webhttp.js`、`DateUtil.js` 等为未完成或遗留文件;数据库连接与密钥直接写于配置/代码中(演示用途)。

---

## 文档索引

- **需求分析**:`需求分析/直播模块/`(直播模块 v1.0 需求、交互逻辑图、视频流流转图)
- **系统设计**:`系统设计/`(整体系分 1.0/2.0、请求模块、订阅模块的并发与系分、课程模块的日程管理与删除课程、直播模块的 WebRTC 时序图与直播间表 PDM)
- **数据库设计**:`sql/`(无限分类设计说明、RBAC 权限模型图、库表建模草稿)
