package gr.hua.dit.dissys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.repository.UserRepository;
import javax.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService{

	@Autowired
    private UserRepository userRepository;
	
	@Override
	@Transactional
	public List<AverageUser> getTenants() {
        return userRepository.findAll();
	}

	@Override
	@Transactional
	public void saveTenant(AverageUser tenant) {
        userRepository.save(tenant);		
	}

	@Override
	@Transactional
	public AverageUser findTenant(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteTenant(int id) {
        userRepository.deleteById(id);		
	}

}
