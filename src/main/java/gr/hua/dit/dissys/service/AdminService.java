package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.UserRegistration;

public interface AdminService {
	
	public List<UserRegistration> getAdmins();

	public void saveAdmin(UserRegistration admin);

	public UserRegistration findAdminById(int id);
	
    public void deleteAdminById(int id);

}
