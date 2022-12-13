package gr.hua.dit.dissys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.repository.TenantRepository;
import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private TenantRepository tenantRepository;
	
	@Override
	@Transactional
	public List<Tenant> getTenants() {
        return tenantRepository.findAll();
	}

	@Override
	@Transactional
	public void saveTenant(Tenant tenant) {
        tenantRepository.save(tenant);		
	}

	@Override
	@Transactional
	public Tenant findTenant(int id) {
		return tenantRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteTenant(int id) {
        tenantRepository.deleteById(id);		
	}

}
