package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;

public interface AdminService {

	public void saveAdmin(AverageUser admin);

	public AverageUser findAdminById(int id);
	
    public void deleteAdminById(int id);

}
