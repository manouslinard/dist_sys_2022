package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.AverageUser;

public interface AdminService {
	
	public List<AverageUser> getAdmins();

	public void saveAdmin(AverageUser admin);

	public AverageUser findAdminById(int id);
	
    public void deleteAdminById(int id);

}
