package gr.hua.dit.dissys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

@RestController
@RequestMapping("/user")
public class UserFrontFunc {

	@Autowired
	private TenantService tenantService;

	@Autowired
	private LessorService lessorService;
	
	@Autowired
	private LeaseService leaseService;
	
	@DeleteMapping("/tenant/{id}")
	public void deleteTenantById(@PathVariable int id) {
		tenantService.deleteTenantById(id);
	}

	@DeleteMapping("/lessor/{id}")
	public void deleteLessorById(@PathVariable int id) {
		lessorService.deleteLessorById(id);
	}

	
	@DeleteMapping("/leases/{id}")
	public void deleteLeaseById(@PathVariable int id) {
		Lease lease = leaseService.findLease(id);
		List <AverageUser> leaseUsers = lease.getUsers();
		// removes lease from users (removes all references):
		for (AverageUser u: leaseUsers) {
			u.getUserLeases().remove(lease);
		}
		// removes it from db:
		leaseService.deleteLease(lease.getId());
	}

}
