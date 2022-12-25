package gr.hua.dit.dissys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.repository.UserRepository;
import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private UserRepository userRepository;
	
	@SuppressWarnings("unlikely-arg-type")
	private boolean isTenant(Set<Role> userRoles) {
		for(Role r:userRoles) {
			if(r.getName().name().equals("ROLE_TENANT")) {
				return true;
			}
		}
		return false;
	}

	
	@Override
	@Transactional
	public List<AverageUser> getTenants() {
		List<AverageUser> users = userRepository.findAll();
		List<AverageUser> tenants = new ArrayList<>();

		for(AverageUser u:users) {
			if(isTenant(u.getRoles())) {
				tenants.add(u);
			}
		}

		return tenants;
	}

	@Override
	@Transactional
	public void saveTenant(AverageUser tenant) {
        userRepository.save(tenant);		
	}

	@Override
	@Transactional
	public AverageUser findTenant(int id) {
		List<AverageUser> tenants = getTenants();
		for(AverageUser t: tenants) {
			if(t.getId() == id) {
				return t;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");	
	}

	@Override
	@Transactional
	public void deleteTenant(int id) {
        userRepository.deleteById(id);		
	}

}
