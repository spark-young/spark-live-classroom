/**
 * 后端返回的状态码和响应消息
 */
export default {
    SUCCESS: {
        code: "200",
        message: "成功"
    },
    DATABASE_UPDATE_FAIL: {
        code: "998",
        message: "数据库更新失败"
    },
    USER_ACCOUNT_ALREADY_EXIST: {
        code: "2008",
        message: "账号已存在"
    },
    USER_IS_UNACTIVED: {
        code: "2009",
    },
    USER_ACCOUNT_NOT_EXIST: {
        code: "2007",
        message: "账号不存在"
    },
    USER_CREDENTIALS_ERROR: {
        code: "2003",
        message: "密码错误"
    },
    COURSE_ALREADY_SELECTED: {
        code: "3001",
        message: "课程已被订阅"
    },
    REQUEST_HAS_EXISTED: {
        code: "3002"
    },
    DATABASE_DELETE_FAIL: {
        code: '996',
        message: "数据库删除失败"
    },
    DATABADATABASE_INSERT_FAILSE_DELETE_FAIL: {
        code: '995',
        message: "数据库插入失败"
    },
    COMMON_FAIL: {
        code: '999'
    }
}