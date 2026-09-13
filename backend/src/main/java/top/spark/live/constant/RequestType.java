package top.spark.live.constant;

import org.springframework.context.annotation.Bean;

import java.util.HashMap;

public class RequestType {
    public static final long DELETE_COURSE = 1;
    public static final long CHANGE_LESSON_TIME = 2;
    public static final long NOTIFICATION = 3;
    public static final String DEL_COURSE = "取消订阅课程";
    public static final String CHA_LESSON_TIME = "变更开课时间";
    public static final String NOTICE = "知会";
    public static HashMap<Long,String> map = new HashMap<Long,String>(){
        {
            put(DELETE_COURSE,DEL_COURSE);
            put(CHANGE_LESSON_TIME,CHA_LESSON_TIME);
            put(NOTIFICATION,NOTICE);
        }
    };
}
