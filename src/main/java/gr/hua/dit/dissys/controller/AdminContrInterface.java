package gr.hua.dit.dissys.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;

public interface AdminContrInterface {
	
	public List<UserRegistration> getAllUsers();
	
	public UserRegistration findUser(String username);
	
	public ResponseEntity<MessageResponse> deleteUser(String username);

	public UserRegistration createLessor(UserRegistration user);

	public UserRegistration createTenant(UserRegistration user);
	
	public UserRegistration createAdmin(UserRegistration user);
	
	public UserRegistration updateLease(UserRegistration user, String username);

}
