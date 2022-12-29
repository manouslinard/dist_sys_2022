package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.AverageUser;

public interface TenantService {

    public List<AverageUser> getTenants();
    public void saveTenant(AverageUser tenant);

    public AverageUser findTenantById(int id);

    public void deleteTenantById(int id);

    public AverageUser findTenant(String username);
	
}
