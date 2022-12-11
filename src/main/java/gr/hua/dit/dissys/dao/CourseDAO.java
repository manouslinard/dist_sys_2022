package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Course;

import java.util.List;

public interface CourseDAO {

    public List<Course> findAll();
    public void save(Course course);

    public Course findById(int id);
}
