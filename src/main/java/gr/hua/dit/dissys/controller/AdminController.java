package gr.hua.dit.dissys.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminController implements AdminContrInterface{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@GetMapping(("/getAllUsers"))
	public List<UserRegistration> getAllUsers() {
		return userRepository.findAll();
	}

    @Autowired
    private RoleRepository roleRepository;
	
	@Override
	@GetMapping("/{username}/findUser")
	public UserRegistration findUser(@PathVariable String username) {	
		return userRepository.findByUsername(username).get();
	}

	@Override
	@DeleteMapping("/{username}/deleteUser")
	public ResponseEntity<MessageResponse> deleteUser(@PathVariable String username) {
		userRepository.delete(findUser(username));
		return ResponseEntity.ok(new MessageResponse("Requested user has been deleted."));
	}

	@Override
	@PostMapping("/createTenant")
	public UserRegistration createTenant(@Valid @RequestBody UserRegistration user) {
		Long id = (long) 0;
		user.setId(id);
		Role role = roleRepository.findByName(ERole.ROLE_TENANT).get();
		user.getRoles().add(role);
		userRepository.save(user);
		return user;
	}
	
	@Override
	@PostMapping("/createLessor")
	public UserRegistration createLessor(@Valid @RequestBody UserRegistration user) {
		Long id = (long) 0;
		user.setId(id);
		Role role = roleRepository.findByName(ERole.ROLE_LESSOR).get();
		user.getRoles().add(role);
		userRepository.save(user);
		return user;
	}
	
	@Override
	@PostMapping("/createAdmin")
	public UserRegistration createAdmin(@Valid @RequestBody UserRegistration user) {
		Long id = (long) 0;
		user.setId(id);
		Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		user.getRoles().add(role);
		userRepository.save(user);
		return user;
	}

	private boolean checkNullEmptyBlank(String strToCheck) {
		// check whether the given string is null or empty or blank
		if (strToCheck == null || strToCheck.isEmpty() || strToCheck.isBlank()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	@PutMapping("/{username}/update")
	public UserRegistration updateLease(@Valid @RequestBody UserRegistration user, @PathVariable String username) {

		UserRegistration oldUser = findUser(username);

		if (!checkNullEmptyBlank(user.getFirstName())) {
			oldUser.setFirstName(user.getFirstName());
		}
		if (!checkNullEmptyBlank(user.getLastName())) {
			oldUser.setLastName(user.getLastName());
		}
		if (!checkNullEmptyBlank(user.getAfm())) {
			oldUser.setAfm(user.getAfm());
		}
		if (!checkNullEmptyBlank(user.getPhone())) {
			oldUser.setPhone(user.getPhone());
		}
		if (!user.getRoles().isEmpty()) {
			oldUser.setRoles(user.getRoles());
		}
		userRepository.save(oldUser);
		return oldUser;

	}

	
}
