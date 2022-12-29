package gr.hua.dit.dissys.entity.basicauth;

import java.util.List;

import gr.hua.dit.dissys.entity.UserRegistration;

public interface UserAuthServ {

	public void saveUser(UserRegistration user);

	public void deleteUser(String username);
	
	public List<User> getAllUsers();
	
}
