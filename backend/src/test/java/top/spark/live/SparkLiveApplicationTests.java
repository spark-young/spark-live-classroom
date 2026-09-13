package top.spark.live;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.spark.live.dto.ChapterNoteDeleteDTO;
import top.spark.live.dto.ChapterRootDTO;
import top.spark.live.dto.CourseInfoDTO;
import top.spark.live.dto.ScheduleDayDTO;
import top.spark.live.entity.Chapter;
import top.spark.live.entity.CourseSelection;
import top.spark.live.mapper.ChapterMapper;
import top.spark.live.mapper.CourseMapper;
import top.spark.live.mapper.CourseSelectionMapper;
import top.spark.live.mapper.ScheduleMapper;
import top.spark.live.service.CourseService;
import top.spark.live.service.ScheduleService;
import top.spark.live.vo.*;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SparkLiveApplicationTests {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseService courseService;
    @Test
    void test(){
        System.out.println(JSON.toJSONString(courseService.getLessonInfo(145)));
    }
}
