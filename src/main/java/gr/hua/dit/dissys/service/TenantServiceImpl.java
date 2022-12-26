package gr.hua.dit.dissys.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;
import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private UserRepository userRepository;
	
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
	
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
    	Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_TENANT)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);		
		tenant.setRoles(roles);
		
        registerTenant(tenant);
        userRepository.save(tenant);
	}

	private void registerTenant(AverageUser tenant) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	Set<Role> auth = tenant.getRoles();
    	for(Role a: auth) {
            authorities.add(new SimpleGrantedAuthority(a.getName().name()));    		
    	}
        
        User user = new User(tenant.getUsername(), passwordEncoder.encode(tenant.getPassword()), authorities);
        //System.out.println(userRegistrationObject.getRole());
        jdbcUserDetailsManager.createUser(user);
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
		String t_username = findTenant(id).getUsername();
		jdbcUserDetailsManager.deleteUser(t_username);
        userRepository.deleteById(id);
	}

}
