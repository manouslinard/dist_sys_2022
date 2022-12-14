package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessor")
public class LessorController {

    @Autowired
    private LessorService lessorService;
    
    @Autowired
    private TenantService tenantService;

    @GetMapping("/getAllTenants")
    public List<Tenant> getAll()
    {
    	return tenantService.getTenants();
    }

    @PostMapping("")
    public Lessor save(@Valid @RequestBody Lessor lessor) {
        lessor.setId(0);
        lessorService.saveLessor(lessor);
        return lessor;
    }

    @GetMapping("/{id}")
    public Lessor get(@PathVariable int id) {
        return lessorService.findLessor(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        lessorService.deleteLessor(id);
    }
}



