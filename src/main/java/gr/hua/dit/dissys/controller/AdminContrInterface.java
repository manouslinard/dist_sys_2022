package gr.hua.dit.dissys.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;

public interface AdminContrInterface {
	
	public List<UserRegistration> getAllUsers();
	
	public UserRegistration findUser(String username);
	
	public ResponseEntity<MessageResponse> deleteUser(String username);

	public UserRegistration createUser(UserRegistration user);
	
}
