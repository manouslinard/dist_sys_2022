package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.dao.TeacherDAO;
import gr.hua.dit.dissys.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherDAO teacherDAO;

    @Override
    @Transactional
    public List<Teacher> getTeachers() {
        return teacherDAO.findAll();
    }

    @Override
    @Transactional
    public void saveTeacher(Teacher teacher) {
        teacherDAO.save(teacher);
    }

    @Override
    @Transactional
    public Teacher findTeacher(int id) {
      return  teacherDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteTeacher(int id) {
        teacherDAO.delete(id);
    }
}
