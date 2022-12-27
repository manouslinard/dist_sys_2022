package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private RoleRepository roleRepository;
    
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
    public List<AverageUser> getLessors() {
        List<AverageUser> users = userRepository.findAll();
        List<AverageUser> lessors = new ArrayList<>();
        
        for(AverageUser u:users) {
        	if(isLessor(u.getRoles())) {
        		lessors.add(u);
        	}
        }
        
        return lessors;
    }

    @Override
    @Transactional
    public void saveLessor(AverageUser lessor) {
    	Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_LESSOR)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);		
		lessor.setRoles(roles);
		if (lessor.getPassword() != null) {
			lessor.setPassword(passwordEncoder.encode(lessor.getPassword()));
		}
        registerLessor(lessor);
        userRepository.save(lessor);		
	}

	private void registerLessor(AverageUser lessor) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	Set<Role> auth = lessor.getRoles();
    	for(Role a: auth) {
            authorities.add(new SimpleGrantedAuthority(a.getName().name()));    		
    	}
        
        User user = new User(lessor.getUsername(), lessor.getPassword(), authorities);
        //System.out.println(userRegistrationObject.getRole());
        jdbcUserDetailsManager.createUser(user);
    }

	
    @Override
    @Transactional
    public AverageUser findLessor(int id) {
    	List<AverageUser> lessors = getLessors();
    	for(AverageUser l: lessors) {
    		if(l.getId() == id) {
    			return l;
    		}
    	}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");    
	}

    @Override
    @Transactional
    public void deleteLessor(int id) {
		String l_username = findLessor(id).getUsername();
		jdbcUserDetailsManager.deleteUser(l_username);
        userRepository.deleteById(id);
    }
}
