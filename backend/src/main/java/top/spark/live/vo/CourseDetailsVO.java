package top.spark.live.vo;

import lombok.Data;

@Data
public class CourseDetailsVO {
    private long tid;
    private String teacherName;

    private String courseName;
    private String courseDescription;
    private String courseUrl;
    private String chapterJSON;

    public CourseDetailsVO(long tid, String teacherName, String courseName, String courseDescription, String courseUrl) {
        this.tid = tid;
        this.teacherName = teacherName;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseUrl = courseUrl;
    }

    @Override
    public String toString() {
        return "CourseDetailsVO{" +
                "tid=" + tid +
                ", teacherName='" + teacherName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseUrl='" + courseUrl + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", chapterJSON='" + chapterJSON + '\'' +
                '}';
    }
}
