package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.dao.TeacherDAO;
import gr.hua.dit.dissys.entity.Lessor;
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
    public List<Lessor> getTeachers() {
        return teacherDAO.findAll();
    }

    @Override
    @Transactional
    public void saveTeacher(Lessor teacher) {
        teacherDAO.save(teacher);
    }

    @Override
    @Transactional
    public Lessor findTeacher(int id) {
      return  teacherDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteTeacher(int id) {
        teacherDAO.delete(id);
    }
}
