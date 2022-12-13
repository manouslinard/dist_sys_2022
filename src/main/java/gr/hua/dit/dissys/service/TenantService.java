package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.Tenant;

public interface TenantService {

    public List<Tenant> getTenants();
    public void saveTenant(Tenant tenant);

    public Tenant findTenant(int id);

    public void deleteTenant(int id);
	
}
