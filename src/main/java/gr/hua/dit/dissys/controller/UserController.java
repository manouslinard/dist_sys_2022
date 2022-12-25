package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.UserRegistration;
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

	private static boolean setLessorAcc = false;
	
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void checkGroupCreate(String groupName, List<GrantedAuthority> authorities) {
        List<String> groups = jdbcUserDetailsManager.findAllGroups();
        for (String g: groups) {
        	if (g.equals(groupName)) {
        		return;	// group exists already -> does nothing
        	}
        }    	
        // group does not exist, creates it and add list of authorities.
        jdbcUserDetailsManager.createGroup(groupName, authorities);
    }
    
    @RequestMapping(value = "/register/setLessor", method = RequestMethod.POST)
    public boolean setLessor() {
    	setLessorAcc = !setLessorAcc;
    	System.out.println(setLessorAcc);
        return setLessorAcc;
    }
    
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("registration", "user", new UserRegistration());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegister(@ModelAttribute("user") UserRegistration userRegistrationObject) {
        // authorities to be granted
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        User user = new User(userRegistrationObject.getUsername(), passwordEncoder.encode(userRegistrationObject.getPassword()), authorities);
        System.out.println(userRegistrationObject.getRole());
        jdbcUserDetailsManager.createUser(user);
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
