package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Teacher;
import gr.hua.dit.dissys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("")
    List<Teacher> getall() {
        return teacherService.getTeachers();
    }

    @PostMapping("")
    Teacher save(@Valid @RequestBody Teacher teacher) {
        teacher.setId(0);
        teacherService.saveTeacher(teacher);
        return teacher;
    }

    @GetMapping("/{id}")
    Teacher get(@PathVariable int id) {
        Teacher teacher = teacherService.findTeacher(id);
        return teacher;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        teacherService.deleteTeacher(id);
    }

}
