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

import gr.hua.dit.dissys.entity.AverageUser;
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
    private JdbcUserDetailsManager jdbcUserDetailsManager;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
		
	@Override
	@Transactional
	public void saveAdmin(AverageUser admin) {
		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);		
		admin.setRoles(roles);
//		if (admin.getPassword() != null) {
//			admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//		}
        userRepository.save(admin);		
		registerAdmin(admin);

	}
	
	private void registerAdmin(AverageUser admin) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	Set<Role> auth = admin.getRoles();
    	for(Role a: auth) {
            authorities.add(new SimpleGrantedAuthority(a.getName().name()));    		
    	}
        
        User user = new User(admin.getUsername(), admin.getPassword(), authorities);
        //System.out.println(userRegistrationObject.getRole());
        jdbcUserDetailsManager.createUser(user);
    }

	@Override
	@Transactional
	public AverageUser findAdminById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteAdminById(int id) {
		String l_username = findAdminById(id).getUsername();
		jdbcUserDetailsManager.deleteUser(l_username);
        userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public List<AverageUser> getAdmins() {
		List<AverageUser> users = userRepository.findAll();
		List<AverageUser> admins = new ArrayList<>();

		for(AverageUser u:users) {
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
