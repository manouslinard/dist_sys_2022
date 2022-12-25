package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LessorServiceImpl implements LessorService{

    @Autowired
    private UserRepository userRepository;
//
//    @Autowired
//    private JdbcUserDetailsManager jdbcUserDetailsManager;

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
        userRepository.save(lessor);
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
        userRepository.deleteById(id);
    }
}
