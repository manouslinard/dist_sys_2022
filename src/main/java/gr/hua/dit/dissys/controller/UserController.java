package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    
	@Autowired
	private TenantService tenantService;

	@Autowired
	private LessorService lessorService;
	
    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET)
    public ModelAndView registerTenant() {
        return new ModelAndView("registration-tenant", "user", new UserRegistration());
    }

    @RequestMapping(value = "/registerTenant", method = RequestMethod.POST)
    public ModelAndView processRegisterTenant(@ModelAttribute("user") UserRegistration userRegistration) {
    	// if user is tenant:
    	setBlankAttr(userRegistration);
    	tenantService.saveTenant(userRegistration);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/registerLessor", method = RequestMethod.GET)
    public ModelAndView registerLessor() {
        return new ModelAndView("registration", "user", new UserRegistration());
    }

    @RequestMapping(value = "/registerLessor", method = RequestMethod.POST)
    public ModelAndView processRegisterLessor(@ModelAttribute("user") UserRegistration userRegistration) {
    	// if user is lessor:
    	setBlankAttr(userRegistration);
    	//System.out.println(userRegistration.getPassword());
    	//System.out.println(userRegistration.getPhone() +", " + userRegistration.getAfm());
    	lessorService.saveLessor(userRegistration);
        return new ModelAndView("redirect:/");
    }
    
    private void setBlankAttr(UserRegistration user) {
    	if (checkNullEmptyBlank(user.getPhone())) {
    		user.setPhone(null);
    	}
    	if (checkNullEmptyBlank(user.getAfm())) {
    		user.setAfm(null);
    	}
    	if (checkNullEmptyBlank(user.getUsername())) {
    		user.setUsername(null);
    	}
    	if (checkNullEmptyBlank(user.getEmail())) {
    		user.setEmail(null);
    	}
    	if (checkNullEmptyBlank(user.getPassword())) {
    		user.setPassword(null);
    	}
    	if (checkNullEmptyBlank(user.getFirstName())) {
    		user.setFirstName(null);
    	}
    	if (checkNullEmptyBlank(user.getLastName())) {
    		user.setLastName(null);
    	}
    }
    
    
	private boolean checkNullEmptyBlank(String strToCheck) {
		// check whether the given string is null or empty or blank
		if (strToCheck == null || strToCheck.isEmpty() || strToCheck.isBlank()) {
			return true;
		} else {
			return false;
		}
	}
    

}
