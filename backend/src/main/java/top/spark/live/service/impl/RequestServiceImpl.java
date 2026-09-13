package top.spark.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.constant.Direction;
import top.spark.live.constant.RequestStatus;
import top.spark.live.constant.RequestType;
import top.spark.live.dto.RequestSolveDTO;
import top.spark.live.entity.CourseSelection;
import top.spark.live.entity.Request;
import top.spark.live.entity.Schedule;
import top.spark.live.mapper.CourseSelectionMapper;
import top.spark.live.mapper.RequestMapper;
import top.spark.live.mapper.ScheduleMapper;
import top.spark.live.mapper.UserMapper;
import top.spark.live.service.RequestService;
import top.spark.live.vo.RequestVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Override
    public List<RequestVO> getTeacherRequest(long tid) {
        QueryWrapper<Request> qw = new QueryWrapper<>();

        List<Integer> direction = new ArrayList<>();
        direction.add(Direction.STUDENT_TO_TEACHER);
        direction.add(Direction.ADMIN_TO_ALL);

        qw.lambda().eq(Request::getTid, tid).in(Request::getDirection, direction).eq(Request::getStatus, RequestStatus.NOT_SOLVE);
        List<Request> list = requestMapper.selectList(qw);

        String tName = userMapper.selectById(tid).getUserId();
        List<RequestVO> result = new ArrayList<>();

        for (Request request : list) {
            RequestVO item = new RequestVO();
            item.setId(request.getId());
            item.setRequestName(tName + " → " + userMapper.selectById(request.getSid()).getUserId());
            item.setRequestType(RequestType.map.get(request.getRid()));

            if(!request.getParamJson().equals("")){
                String json = request.getParamJson();
                Object parse = JSON.parse(json);
                Map<String, Object> map = JSON.parseObject(parse.toString(), Map.class);
                switch (RequestType.map.get(request.getRid())) {
                    case RequestType.CHA_LESSON_TIME:
                        item.setRequestContent("请求将: “" + map.get("courseName") + " → " + map.get("chapterName") + "” 的开课时间调整为“" + map.get("changeTime") + "”");
                        break;
                    case RequestType.DEL_COURSE:
                        item.setRequestContent("“" + map.get("sName") + "”请求取消订阅“" + map.get("courseName") + "”");
                        break;
                    case RequestType.NOTICE:
                        StringBuilder content = new StringBuilder("");
                        Request request1 = requestMapper.selectById(Long.valueOf(map.get("id").toString()));
                        switch (RequestType.map.get(request1.getRid())) {
                            case RequestType.CHA_LESSON_TIME:
                                Object obj = JSON.parse(request1.getParamJson());
                                Map<String, Object> map1 = JSON.parseObject(obj.toString(), Map.class);
                                content.append("对“").append(userMapper.selectById(request1.getSid()).getUserId()).append("“,将").append(map1.get("courseName")).append(" → ")
                                        .append(map1.get("chapterName"))
                                        .append("的开课时间调整为").append(map1.get("changeTime")).append("，处理结果为：").append(map.get("result"));
                                break;
                        }
                        item.setRequestContent(content.toString());
                }
            }

            item.setRequestReason(request.getContent());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<RequestVO> getStudentRequest(long sid) {
        QueryWrapper<Request> qw = new QueryWrapper<>();

        List<Integer> direction = new ArrayList<>();
        direction.add(Direction.TEACHER_TO_STUDENT);
        direction.add(Direction.ADMIN_TO_ALL);
        qw.lambda().eq(Request::getSid, sid).in(Request::getDirection, direction).eq(Request::getStatus, RequestStatus.NOT_SOLVE);
        List<Request> list = requestMapper.selectList(qw);

        String sName = userMapper.selectById(sid).getUserId();
        List<RequestVO> result = new ArrayList<>();

        list.forEach(request -> {
            RequestVO item = new RequestVO();
            item.setId(request.getId());
            item.setRequestName(sName + " → " + userMapper.selectById(request.getTid()).getUserId());
            item.setRequestType(RequestType.map.get(request.getRid()));

            if(!request.getParamJson().equals("")){
                String json = request.getParamJson();
                Object parse = JSON.parse(json);
                Map<String, Object> map = JSON.parseObject(parse.toString(), Map.class);
                switch (RequestType.map.get(request.getRid())) {
                    case RequestType.CHA_LESSON_TIME:
                        item.setRequestContent("请求将: “" + map.get("courseName") + " → " + map.get("chapterName") + "” 的开课时间调整为“" + map.get("changeTime") + "”");
                        break;
                    case RequestType.DEL_COURSE:
                        item.setRequestContent("“" + map.get("sName") + "”请求取消订阅“" + map.get("courseName") + "”");
                        break;
                    case RequestType.NOTICE:
                        StringBuilder content = new StringBuilder("");
                        Request request1 = requestMapper.selectById(Long.valueOf(map.get("id").toString()));
                        System.out.println(map.get("id") + "!!!!!!!!!!");
                        switch (RequestType.map.get(request1.getRid())) {
                            case RequestType.CHA_LESSON_TIME:
                                Object obj = JSON.parse(request1.getParamJson());
                                Map<String, Object> map1 = JSON.parseObject(obj.toString(), Map.class);
                                content.append("对“").append(userMapper.selectById(request1.getTid()).getUserId()).append("“，将").append(map1.get("courseName")).append(" → ")
                                        .append(map1.get("chapterName"))
                                        .append("的开课时间调整为").append(map1.get("changeTime")).append("，处理结果为：").append(map.get("result"));
                                break;
                            case RequestType.DEL_COURSE:
                                Object objDel = JSON.parse(request1.getParamJson());
                                Map<String, Object> mapDel = JSON.parseObject(objDel.toString(), Map.class);
                                content.append("对").append(userMapper.selectById(request1.getTid()).getUserId()).append("“，将“").append(mapDel.get("courseName")).append("”课程取消的处理结果为：")
                                        .append(map.get("result"));
                                break;
                        }
                        item.setRequestContent(content.toString());
                }
            }

            item.setRequestReason(request.getContent());
            result.add(item);
        });
        return result;
    }


    @Override
    public long getCountTeacher(long tid) {
        QueryWrapper<Request> qw = new QueryWrapper<>();
        qw.lambda().eq(Request::getTid, tid).eq(Request::getDirection, Direction.STUDENT_TO_TEACHER).eq(Request::getStatus, RequestStatus.NOT_SOLVE);
        return requestMapper.selectCount(qw);
    }

    @Override
    public long getCountStudent(long sid) {
        QueryWrapper<Request> qw = new QueryWrapper<>();
        qw.lambda().eq(Request::getSid, sid).eq(Request::getDirection, Direction.TEACHER_TO_STUDENT).eq(Request::getStatus, RequestStatus.NOT_SOLVE);
        return requestMapper.selectCount(qw);
    }

    @Override
    public boolean solveRequest(RequestSolveDTO requestSolveDTO) {
        Request request = requestMapper.selectById(requestSolveDTO.getId());
        System.out.println(JSON.toJSONString(requestSolveDTO));

        JSONObject noticeObject = new JSONObject();
        noticeObject.put("id", requestSolveDTO.getId());

        switch (RequestType.map.get(request.getRid())) {
            case RequestType.NOTICE://已知会，需要删除知会的请求和该请求
                if(!request.getParamJson().equals("")){
                    Map<String, Object> map = JSON.parseObject(JSON.parse(request.getParamJson()).toString(), Map.class);
                    requestMapper.deleteById(Long.valueOf(map.get("id").toString()));
                }
                requestMapper.deleteById(requestSolveDTO.getId());
                return true;
            case RequestType.DEL_COURSE:
                Map<String, Object> mapDel = JSON.parseObject(JSON.parse(request.getParamJson()).toString(), Map.class);
                QueryWrapper<CourseSelection> qw1 = new QueryWrapper<>();
                long cid = Long.valueOf(mapDel.get("cid").toString());
                long sid = Long.valueOf(mapDel.get("sid").toString());
                qw1.lambda().eq(CourseSelection::getCid, cid).eq(CourseSelection::getSid, sid);
                courseSelectionMapper.delete(qw1);

                QueryWrapper<Schedule> qw2 = new QueryWrapper<>();
                qw2.lambda().eq(Schedule::getCid, cid).eq(Schedule::getSid, sid);
                scheduleMapper.delete(qw2);
                break;
            case RequestType.CHA_LESSON_TIME:
                Map<String, Object> mapCha = JSON.parseObject(JSON.parse(request.getParamJson()).toString(), Map.class);
                QueryWrapper<Schedule> qw3 = new QueryWrapper<>();
                qw3.lambda().eq(Schedule::getCid, Long.valueOf(mapCha.get("cid").toString())).eq(Schedule::getChid, Long.valueOf(mapCha.get("chid").toString()))
                        .eq(Schedule::getSid, request.getSid());
                Schedule schedule = scheduleMapper.selectOne(qw3);
                String[] changeTime = mapCha.get("changeTime").toString().replaceAll(" ", "-").replaceAll("T", "-").replaceAll(":", "-").split("-");
                schedule.setClassTime(LocalDateTime.of(Integer.valueOf(changeTime[0]), Integer.valueOf(changeTime[1]), Integer.valueOf(changeTime[2]),
                        Integer.valueOf(changeTime[3]), Integer.valueOf(changeTime[4]), Integer.valueOf(changeTime[5])));
                scheduleMapper.update(schedule,qw3);
                break;
        }

        //发送知会请求
        noticeObject.put("result", requestSolveDTO.isAccept() ? "通过" : "驳回");
        String paramJSON = noticeObject.toJSONString();

        Request notice = new Request();
        notice.setTid(request.getTid());
        notice.setSid(request.getSid());
        notice.setRid(RequestType.NOTIFICATION);
        notice.setDirection(request.getDirection() == Direction.TEACHER_TO_STUDENT ? Direction.STUDENT_TO_TEACHER : Direction.TEACHER_TO_STUDENT);
        notice.setContent(requestSolveDTO.getContent());
        notice.setParamJson(paramJSON);
        notice.setStatus(RequestStatus.NOT_SOLVE);
        requestMapper.insert(notice);
        request.setStatus(RequestStatus.SOLVED);
        requestMapper.updateById(request);

        return true;
    }

}
