package gr.hua.dit.dissys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private UserRepository tenantRepository;
	
	@Override
	@Transactional
	public List<UserRegistration> getTenants() {
        return tenantRepository.findAll();
	}

	@Override
	@Transactional
	public void saveTenant(UserRegistration tenant) {
        tenantRepository.save(tenant);		
	}


    @Override
    @Transactional
    public UserRegistration findTenant(String username) {
    	return tenantRepository.findByUsername(username).get();
    }

    @Override
    @Transactional
    public void deleteTenant(String username) {
    	if (tenantRepository.existsByUsername(username)) {
    		UserRegistration u = tenantRepository.findByUsername(username).get();
    		tenantRepository.delete(u);
    	}
    }
}
