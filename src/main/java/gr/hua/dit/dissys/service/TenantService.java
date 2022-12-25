package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.AverageUser;

public interface TenantService {

    public List<AverageUser> getTenants();
    public void saveTenant(AverageUser tenant);

    public AverageUser findTenant(int id);

    public void deleteTenant(int id);
	
}
