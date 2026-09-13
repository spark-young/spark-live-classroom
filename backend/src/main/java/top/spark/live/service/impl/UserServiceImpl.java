package top.spark.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.spark.live.dto.NoteDTO;
import top.spark.live.dto.NoteModifyDTO;
import top.spark.live.dto.UserInfoDTO;
import top.spark.live.entity.Note;
import top.spark.live.entity.User;
import top.spark.live.mapper.NoteMapper;
import top.spark.live.mapper.UserMapper;
import top.spark.live.service.UserService;
import top.spark.live.vo.NoteVO;
import top.spark.live.vo.StudentInfoVO;
import top.spark.live.vo.TeacherInfoVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoteMapper noteMapper;
    @Override
    public TeacherInfoVO getTeacherInfo(long tid) {
        User user = userMapper.selectById(tid);
        TeacherInfoVO teacherInfoVO = new TeacherInfoVO();
        teacherInfoVO.setId(user.getId());
        teacherInfoVO.setTeacherName(user.getUserId());
        teacherInfoVO.setEmail(user.getEmail());
        teacherInfoVO.setMobile(user.getMobile());
        return teacherInfoVO;
    }

    @Override
    public StudentInfoVO getStudentInfo(long sid) {
        User user = userMapper.selectById(sid);
        StudentInfoVO studentInfoVO = new StudentInfoVO();
        studentInfoVO.setId(user.getId());
        studentInfoVO.setStudentName(user.getUserId());
        studentInfoVO.setEmail(user.getEmail());
        studentInfoVO.setMobile(user.getMobile());
        return studentInfoVO;
    }

    @Override
    public boolean modifyUserInfo(UserInfoDTO userInfoDTO) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getId,userInfoDTO.getId());
        User user = new User();
        user.setEmail(userInfoDTO.getEmail());
        user.setMobile(userInfoDTO.getMobile());
        int result;
        try{
            result = userMapper.update(user,qw);
        }catch(Exception e){
            return false;
        }
        return result == 1;
    }

    @Override
    public boolean addNewNote(NoteDTO noteDTO) {
        Note note = new Note();
        note.setSid(noteDTO.getSid());
        note.setContent(noteDTO.getContent());
        return noteMapper.insert(note) == 1;
    }

    @Override
    public boolean modifyNote(NoteModifyDTO noteModifyDTO) {
        Note note = noteMapper.selectById(noteModifyDTO.getId());
        note.setContent(noteModifyDTO.getContent());
        return noteMapper.updateById(note) == 1;
    }

    @Override
    public boolean deleteNote(long id) {
        return noteMapper.deleteById(id) == 1;
    }

    @Override
    public List<NoteVO> getNoteList(long sid) {
        QueryWrapper<Note> qw = new QueryWrapper<>();
        qw.lambda().eq(Note::getSid,sid).orderByAsc(Note::getCreateTime);

        List<Note> list = noteMapper.selectList(qw);
        List<NoteVO> result = new ArrayList<>();

        list.forEach((e) -> {
            NoteVO item = new NoteVO();
            item.setId(e.getId());
            item.setContent(e.getContent());
            item.setCreateTime(e.getCreateTime());
            result.add(item);
        });
        return result;
    }

}
