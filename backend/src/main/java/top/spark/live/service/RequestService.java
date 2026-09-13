package top.spark.live.service;

import top.spark.live.dto.RequestSolveDTO;
import top.spark.live.vo.RequestVO;

import java.util.List;

public interface RequestService {
    public List<RequestVO> getTeacherRequest(long tid);
    public List<RequestVO> getStudentRequest(long sid);

    public long getCountTeacher(long tid);
    public long getCountStudent(long sid);

    public boolean solveRequest(RequestSolveDTO requestSolveDTO);//处理需要人工处理并反馈的请求
}
