package top.spark.live.service;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import top.spark.live.dto.NoteDTO;
import top.spark.live.dto.NoteModifyDTO;
import top.spark.live.dto.UserInfoDTO;
import top.spark.live.vo.NoteVO;
import top.spark.live.vo.StudentInfoVO;
import top.spark.live.vo.TeacherInfoVO;

import java.util.List;

public interface UserService {
    public TeacherInfoVO getTeacherInfo(long tid);
    public StudentInfoVO getStudentInfo(long sid);
    public boolean modifyUserInfo(UserInfoDTO userInfoDTO);

    public boolean addNewNote(NoteDTO noteDTO);
    public boolean modifyNote(NoteModifyDTO noteModifyDTO);
    public boolean deleteNote(long id);
    public List<NoteVO> getNoteList(long sid);
}
