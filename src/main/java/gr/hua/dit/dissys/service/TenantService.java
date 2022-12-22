package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.UserRegistration;


public interface TenantService {

    public List<UserRegistration> getTenants();
    public void saveTenant(UserRegistration tenant);

    public UserRegistration findTenant(String id);

    public void deleteTenant(String id);
	
}
