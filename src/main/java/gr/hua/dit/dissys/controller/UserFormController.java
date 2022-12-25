package gr.hua.dit.dissys.controller;

import java.util.HashSet;
import java.util.List;
import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserFormController {

    @Autowired
    private LessorService lessorService;

    @Autowired
    private TenantService tenantService;
    
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/teacherform")
    public String showLessorForm(Model model) {
    	AverageUser lessor = new AverageUser();
        model.addAttribute("teacher", lessor);
        return "add-teacher";
    }

    @GetMapping("/teacherlist")
    public String showLessorList(Model model) {
        List<AverageUser> teachers = lessorService.getLessors();
        model.addAttribute("teachers", teachers);
        return "list-teachers";

    }

    @PostMapping(path = "/teacherform")
    public String saveLessor(@ModelAttribute("teacher") AverageUser teacher) {
        lessorService.saveLessor(teacher);
        return "redirect:/";

    }


    @GetMapping("/tenantform")
    public String showTenantForm(Model model) {
        AverageUser tenant = new AverageUser();
//        HashSet<Role> set= new HashSet();
//        set.add(new Role(ERole.ROLE_TENANT));
//        tenant.setRoles(set);
        model.addAttribute("tenant", tenant);
        return "add-tenant";
    }

    @GetMapping("/tenantlist")
    public String showTenantList(Model model) {
        List<AverageUser> tenants = tenantService.getTenants();
        model.addAttribute("tenants", tenants);
        return "list-tenants";

    }

    @PostMapping(path = "/tenantform")
    public String saveTenant(@ModelAttribute("tenant") AverageUser teacher) {
        tenantService.saveTenant(teacher);
        return "redirect:/";

    }

}