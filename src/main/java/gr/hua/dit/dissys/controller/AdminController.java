package gr.hua.dit.dissys.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;
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
	@PostMapping("/createUser")
	public UserRegistration createUser(@Valid @RequestBody UserRegistration user) {
		Long id = (long) 0;
		user.setId(id);
		userRepository.save(user);
		return user;
	}

}
