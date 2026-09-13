package top.spark.live.service;

import top.spark.live.dto.LessonInfoDTO;
import top.spark.live.dto.ScheduleDayDTO;
import top.spark.live.vo.CalendarVO;
import top.spark.live.vo.ScheduleDetailVO;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    public List<CalendarVO> getCalendarAllTeacher(long tid);
    public List<CalendarVO> getCalendarAllStudent(long sid);

    public int setLessonInfo(LessonInfoDTO lessonInfoDTO);

    public List<ScheduleDetailVO> getScheduleDetailTeacher(ScheduleDayDTO scheduleDayDTO);
    public List<ScheduleDetailVO> getScheduleDetailStudent(ScheduleDayDTO scheduleDayDTO);

    public int getTodayClassCount(long id);
}
