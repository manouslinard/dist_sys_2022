package gr.hua.dit.dissys.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.entity.basicauth.UserAuthServ;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
		
	@Autowired
	private UserAuthServ userAuthServ;
	
	@Override
	@Transactional
	public void saveAdmin(UserRegistration admin) {
		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);		
		admin.setRoles(roles);
		if (admin.getPassword() != null) {
			admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		}
        userRepository.save(admin);		
    	userAuthServ.saveUser(admin);
	}
	
	@Override
	@Transactional
	public UserRegistration findAdminById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteAdminById(int id) {
		String l_username = findAdminById(id).getUsername();
		userAuthServ.deleteUser(l_username);
        userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public List<UserRegistration> getAdmins() {
		List<UserRegistration> users = userRepository.findAll();
		List<UserRegistration> admins = new ArrayList<>();

		for(UserRegistration u:users) {
			if(isAdmin(u.getRoles())) {
				admins.add(u);
			}
		}

		return admins;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	private boolean isAdmin(Set<Role> userRoles) {
		for(Role r:userRoles) {
			if(r.getName().name().equals("ROLE_ADMIN")) {
				return true;
			}
		}
		return false;
	}

}
