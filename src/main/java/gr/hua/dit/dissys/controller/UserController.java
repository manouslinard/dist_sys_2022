package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
	
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
	private TenantService tenantService;

	@Autowired
	private LessorService lessorService;
	
    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET)
    public ModelAndView registerTenant() {
        return new ModelAndView("registration-tenant", "user", new AverageUser());
    }

    @RequestMapping(value = "/registerTenant", method = RequestMethod.POST)
    public ModelAndView processRegisterTenant(@ModelAttribute("user") AverageUser userRegistration) {
    	// if user is tenant:
    	tenantService.saveTenant(userRegistration);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/registerLessor", method = RequestMethod.GET)
    public ModelAndView registerLessor() {
        return new ModelAndView("registration", "user", new AverageUser());
    }

    @RequestMapping(value = "/registerLessor", method = RequestMethod.POST)
    public ModelAndView processRegisterLessor(@ModelAttribute("user") AverageUser userRegistration) {
    	// if user is lessor:
    	lessorService.saveLessor(userRegistration);
        return new ModelAndView("redirect:/");
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }


}
