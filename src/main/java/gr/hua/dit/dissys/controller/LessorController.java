package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.repository.LessorRepository;
import gr.hua.dit.dissys.repository.TenantRepository;
import gr.hua.dit.dissys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessors")
public class LessorController {

    @Autowired
    LessorRepository lessorRepository;

    @GetMapping("")
    public List<Lessor> getAll()
    {
        return lessorRepository.findAll();
    }

    @PostMapping("")
    Lessor save(@Valid @RequestBody Lessor lessor) {
        lessor.setId(0);
        lessorRepository.save(lessor);
        return lessor;
    }

    @GetMapping("/{id}")
    Lessor get(@PathVariable int id) {
        return lessorRepository.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        lessorRepository.deleteById(id);
    }
}



