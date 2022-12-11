package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Course;
import gr.hua.dit.dissys.entity.Teacher;

import java.util.List;

public interface TeacherDAO {

    public List<Teacher> findAll();
    public void save(Teacher teacher);

    public Teacher findById(int id);

    public void delete(int id);
}
