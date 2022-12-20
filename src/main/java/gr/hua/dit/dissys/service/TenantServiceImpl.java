package gr.hua.dit.dissys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gr.hua.dit.dissys.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private UserRepository userRepository;

	@SuppressWarnings("unlikely-arg-type")
	private boolean isTenant(Set<Role> userRoles) {
		for(Role r:userRoles) {
			if(r.getName().equals("ROLE_TENANT")) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public List<UserRegistration> getTenants() {
		List<UserRegistration> users = userRepository.findAll();
		List<UserRegistration> tenants = new ArrayList<>();

		for(UserRegistration u:users) {
			if(isTenant(u.getRoles())) {
				tenants.add(u);
			}
		}

		return tenants;
	}

	@Override
	@Transactional
	public void saveTenant(UserRegistration tenant) {
        userRepository.save(tenant);
	}


	@Override
	@Transactional
	public UserRegistration findTenant(String username) {
		List<UserRegistration> tenants = getTenants();
		for(UserRegistration t: tenants) {
			if(t.getUsername().equals(username)) {
				return t;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

    @Override
    @Transactional
    public void deleteTenant(String username) {
		UserRegistration tenant = findTenant(username);
		userRepository.delete(tenant);
	}
}

