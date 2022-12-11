package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Student;
import gr.hua.dit.dissys.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("")
    public List<Student> getAll()
    {
        return studentRepository.findAll();
    }
}
