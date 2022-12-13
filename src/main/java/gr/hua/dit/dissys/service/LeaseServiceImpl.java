package gr.hua.dit.dissys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.repository.LeaseRepository;
import javax.transaction.Transactional;

@Service
public class LeaseServiceImpl implements LeaseService{

	@Autowired
    private LeaseRepository leaseRepository;
	
	@Override
	@Transactional
	public List<Lease> getLeases() {
		return leaseRepository.findAll();
	}

	@Override
	@Transactional
	public void saveLease(Lease lease) {
        leaseRepository.save(lease);				
	}

	@Override
	@Transactional
	public Lease findLease(int id) {
		return leaseRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteLease(int id) {
        leaseRepository.deleteById(id);		
	}

}
