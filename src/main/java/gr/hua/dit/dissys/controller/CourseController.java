package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.dao.CourseDAO;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
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
    List<Lease> getall() {
        return courseDAO.findAll();
    }

    @PostMapping("")
    Lease save(@RequestBody Lease course) {
        course.setId(0);
        courseDAO.save(course);
        return course;
    }

    @GetMapping("/{id}")
    Lease get(@PathVariable int id) {
        Lease course = courseDAO.findById(id);
        return course;
    }

    @PostMapping("/{cid}/teacher")
    Lessor addTeacher(@PathVariable int cid, @RequestBody Lessor teacher) {
        int teacherId = teacher.getId();
        Lease course = courseDAO.findById(cid);

        if (course == null) {
            throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        );
        }

        if (teacherId != 0) {
            Lessor ateacher = teacherService.findTeacher(teacherId);
            course.setLessor(ateacher);
            teacherService.saveTeacher(teacher);
            return ateacher;
        }

        course.setLessor(teacher);
        teacherService.saveTeacher(teacher);
        return teacher;

    }
}
