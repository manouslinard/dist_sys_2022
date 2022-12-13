package gr.hua.dit.dissys.controller;

import java.util.List;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.service.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TeacherFormController {

    @Autowired
    private LessorService lessorService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/teacherform")
    public String showTeacherForm(Model model) {
        Lessor teacher = new Lessor();
        model.addAttribute("teacher", teacher);
        return "add-teacher";
    }

    @GetMapping("/teacherlist")
    public String showTeacherList(Model model) {
        List<Lessor> teachers = lessorService.getLessors();
        model.addAttribute("teachers", teachers);
        return "list-teachers";

    }

    @PostMapping(path = "/teacherform")
    public String saveTeacher(@ModelAttribute("teacher") Lessor teacher) {
        lessorService.saveLessor(teacher);
        return "redirect:/";

    }
}