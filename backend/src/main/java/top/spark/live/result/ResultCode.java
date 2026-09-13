package top.spark.live.result;

import lombok.Data;

/**
 * 自定义状态码和响应消息(可以自行增加)
 * @author shawshank
 */

public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 常规失败 */
    DATABASE_INSERT_FAIL(995, "数据库插入失败"),
    DATABASE_DELETE_FAIL(996, "数据库删除失败"),
    DATABASE_SEARCH_FAIL(997, "数据库查询失败"),
    DATABASE_UPDATE_FAIL(998, "数据库更新失败"),
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000~1999 */
    PARAM_IS_BLANK(1001, "参数为空"),
    PARAM_NOT_COMPLETE(1002, "参数缺失"),
    PARAM_FORMAT_ERROR(1003, "参数格式错误"),
    PARAM_ERROR(1004, "传入参数错误"),

    /* 用户错误 2000~2999 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_IS_UNACTIVED(2009,"账号已被管理员禁用，请邮箱admin@spark.com联系管理员处理"),

    /* 业务错误 3000~3999 */
    COURSE_ALREADY_SELECTED(3001, "课程已被订阅"),
    REQUEST_HAS_EXISTED(3002,"该请求已存在"),
    SECTION_RESOURCE_NOT_MATCH(3003, "当前课程没有该课程资源"),
    RESOURCE_NODE_INVALID(3004, "无效的资源结点"),
    ALREADY_EXSIST_FILE_OF_SAME_NAME(3005, "当前目录下已经存在同名文件"),
    ALREADY_CORRECTED_CANNOT_SUBMIT_AGAIN(3006, "作业已被批改无法再提交"),
    ALREADY_CORRECTED_CANNOT_CORRECT_AGAIN(3007, "作业已被批改无法再批改");

    private Integer code;    // 状态码
    private String message;    // 响应消息

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
