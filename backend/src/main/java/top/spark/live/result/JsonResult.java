package top.spark.live.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean success;    // 是否成功
    private Integer statusCode;       //状态码
    private String message;     //响应消息
    private Object data;        //携带的数据

    /**
     * 构造方法
     */
    public JsonResult(boolean success) {
        this.success = success;
        this.statusCode = success ? ResultCode.SUCCESS.getCode() : ResultCode.COMMON_FAIL.getCode();
        this.message = success ? ResultCode.SUCCESS.getMessage() : ResultCode.COMMON_FAIL.getMessage();
    }

    public JsonResult(boolean success, ResultCode resultCode) {
        this.success = success;
        this.statusCode = success ? ResultCode.SUCCESS.getCode() : (resultCode == null ? ResultCode.COMMON_FAIL.getCode() : resultCode.getCode());
        this.message = success ? ResultCode.SUCCESS.getMessage() : (resultCode == null ? ResultCode.COMMON_FAIL.getMessage() : resultCode.getMessage());
    }

    public JsonResult(boolean success, Object data) {
        this.success = success;
        this.statusCode = success ? ResultCode.SUCCESS.getCode() : ResultCode.COMMON_FAIL.getCode();
        this.message = success ? ResultCode.SUCCESS.getMessage() : ResultCode.COMMON_FAIL.getMessage();
        this.data = data;
    }

    public JsonResult(boolean success, ResultCode resultCode, Object data) {
        this.success = success;
        this.statusCode = success ? ResultCode.SUCCESS.getCode() : (resultCode == null ? ResultCode.COMMON_FAIL.getCode() : resultCode.getCode());
        this.message = success ? ResultCode.SUCCESS.getMessage() : (resultCode == null ? ResultCode.COMMON_FAIL.getMessage() : resultCode.getMessage());
        this.data = data;
    }
}
