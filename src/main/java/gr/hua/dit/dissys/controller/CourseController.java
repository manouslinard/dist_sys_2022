package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.dao.CourseDAO;
import gr.hua.dit.dissys.entity.Course;
import gr.hua.dit.dissys.entity.Teacher;
import gr.hua.dit.dissys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("")
    List<Course> getall() {
        return courseDAO.findAll();
    }

    @PostMapping("")
    Course save(@RequestBody Course course) {
        course.setId(0);
        courseDAO.save(course);
        return course;
    }

    @GetMapping("/{id}")
    Course get(@PathVariable int id) {
        Course course = courseDAO.findById(id);
        return course;
    }

    @PostMapping("/{cid}/teacher")
    Teacher addTeacher(@PathVariable int cid, @RequestBody Teacher teacher) {
        int teacherId = teacher.getId();
        Course course = courseDAO.findById(cid);

        if (course == null) {
            throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        );
        }

        if (teacherId != 0) {
            Teacher ateacher = teacherService.findTeacher(teacherId);
            course.setTeacher(ateacher);
            teacherService.saveTeacher(teacher);
            return ateacher;
        }

        course.setTeacher(teacher);
        teacherService.saveTeacher(teacher);
        return teacher;

    }
}
