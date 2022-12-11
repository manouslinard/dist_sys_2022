package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.Teacher;

import java.util.List;

public interface TeacherService {

    public List<Teacher> getTeachers();
    public void saveTeacher(Teacher teacher);

    public Teacher findTeacher(int id);

    public void deleteTeacher(int id);
}
