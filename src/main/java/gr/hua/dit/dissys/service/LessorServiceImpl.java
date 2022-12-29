package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.entity.basicauth.UserAuthServ;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LessorServiceImpl implements LessorService{

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserAuthServ userAuthServ;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @SuppressWarnings("unlikely-arg-type")
	private boolean isLessor(Set<Role> userRoles) {
    	for(Role r:userRoles) {
    		if(r.getName().name().equals("ROLE_LESSOR")) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    @Transactional
    public List<UserRegistration> getLessors() {
        List<UserRegistration> users = userRepository.findAll();
        List<UserRegistration> lessors = new ArrayList<>();
        
        for(UserRegistration u:users) {
        	if(isLessor(u.getRoles())) {
        		lessors.add(u);
        	}
        }
        
        return lessors;
    }

    @Override
    @Transactional
    public void saveLessor(UserRegistration lessor) {
    	Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_LESSOR)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);		
		lessor.setRoles(roles);
		if (lessor.getPassword() != null) {
			lessor.setPassword(passwordEncoder.encode(lessor.getPassword()));
		}
        userRepository.save(lessor);		
    	userAuthServ.saveUser(lessor);
	}

    @Override
    @Transactional
    public UserRegistration findLessor(String username) {
    	List<UserRegistration> lessors = getLessors();
    	for(UserRegistration l: lessors) {
    		if(l.getUsername().equals(username)) {
    			return l;
    		}
    	}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }

    @Override
    @Transactional
    public void deleteLessor(String username) {
    	UserRegistration lessor = findLessor(username);
		userAuthServ.deleteUser(username);
		userRepository.delete(lessor);
    }
}
