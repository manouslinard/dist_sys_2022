package gr.hua.dit.dissys.entity.basicauth;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.UserRegistration;

@Service
public class UserAuthServImpl implements UserAuthServ{	
	@Autowired
    private AuthRepo authRepo;
	@Autowired
    private UserRepo userRepo;
    
	@Transactional
	public void saveUser(UserRegistration user) {

		String username = user.getUsername(); 

		User u = new User(username, user.getPassword());
		userRepo.save(u);
		Set<Role> roles = user.getRoles();
		
		for (Role r: roles) {
			Auth a = new Auth(username, r.getName().name());
			authRepo.save(a);
		}
		
	}

	@Transactional
	public void deleteUser(String username) {
		Auth a = authRepo.findByUsername(username).get();
		authRepo.delete(a);
		User u = userRepo.findByUsername(username).get();
		userRepo.delete(u);
	}
	
	@Transactional
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
}
