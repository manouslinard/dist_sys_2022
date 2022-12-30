package gr.hua.dit.dissys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.repository.LeaseRepository;
import org.springframework.web.server.ResponseStatusException;

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
	public Lease findLeaseByTitle(String leaseTitle) {
		List<Lease> leases = leaseRepository.findAll();
		for(Lease lease:leases){
			if(lease.getTitle().equals(leaseTitle)){
				return lease;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lease not found");
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
