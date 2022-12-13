package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.Lessor;

import java.util.List;

public interface TeacherService {

    public List<Lessor> getTeachers();
    public void saveTeacher(Lessor teacher);

    public Lessor findTeacher(int id);

    public void deleteTeacher(int id);
}
