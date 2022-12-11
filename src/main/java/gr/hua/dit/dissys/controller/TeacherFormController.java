package gr.hua.dit.dissys.controller;

import java.util.List;
import gr.hua.dit.dissys.entity.Teacher;
import gr.hua.dit.dissys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TeacherFormController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/teacherform")
    public String showTeacherForm(Model model) {
        Teacher teacher = new Teacher();
        model.addAttribute("teacher", teacher);
        return "add-teacher";
    }

    @GetMapping("/teacherlist")
    public String showTeacherList(Model model) {
        List<Teacher> teachers = teacherService.getTeachers();
        model.addAttribute("teachers", teachers);
        return "list-teachers";

    }

    @PostMapping(path = "/teacherform")
    public String saveTeacher(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/";

    }
}
