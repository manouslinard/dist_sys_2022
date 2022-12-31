package gr.hua.dit.dissys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.UserRepository;
import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.service.AdminService;
import gr.hua.dit.dissys.service.ContractService;
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
	private UserRepository userRepo;
	
	@Autowired
	private LeaseService leaseService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ContractService contractService;
	
	@DeleteMapping("/tenant/{id}")
	public void deleteTenantById(@PathVariable int id) {
		tenantService.deleteTenant(userRepo.findById(id).get().getUsername());
	}

	@DeleteMapping("/lessor/{id}")
	public void deleteLessorById(@PathVariable int id) {
		lessorService.deleteLessor(userRepo.findById(id).get().getUsername());
	}

	
	@DeleteMapping("/leases/{id}")
	public void deleteLeaseById(@PathVariable int id) {
		Lease lease = leaseService.findLease(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String logged_in_username = auth.getName();
        boolean contains_user = false;
        for (UserRegistration u: lease.getUsers()) {
        	if (u.getUsername().equals(logged_in_username)) {
        		contains_user = true;
        	}
        }
        if (!contains_user) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete lease of not logged in lessor!");         	
        }
		List <UserRegistration> leaseUsers = lease.getUsers();
		// removes lease from users (removes all references):
		for (UserRegistration u: leaseUsers) {
			u.getUserLeases().remove(lease);
		}
		// removes it from db:
		leaseService.deleteLease(lease);
	}

	@DeleteMapping("/admin/{id}")
	public void deleteAdminById(@PathVariable int id) {
		UserRegistration reqAdmin= adminService.findAdminById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String logged_in_username = auth.getName();
        if (reqAdmin.getUsername().equals(logged_in_username)) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete logged in admin."); 
        }
		
        adminService.deleteAdminById(id);
	}

	
	@PostMapping("/leases/agree/{id}")
	public void agreeLease(@PathVariable int id) {
		Lease l = leaseService.findLease(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String logged_in_username = auth.getName();
        boolean contains_user = false;
        for (UserRegistration u: l.getUsers()) {
        	if (u.getUsername().equals(logged_in_username)) {
        		contains_user = true;
        	}
        }
        if (!contains_user) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot submit answer of not logged in tenant!");         	
        }
        String title = l.getTitle();
        String dimos = l.getDimos();
        String sp_con = l.getSp_con();
        String reason = l.getReason();
        String address = l.getAddress();
        String tk = l.getTk();
        double cost = l.getCost();
        String startDate = l.getStartDate();
        String endDate = l.getEndDate();
        String dei = l.getDei();
		Contract c = new Contract(title, address, tk, dimos, reason, cost, startDate, endDate, sp_con, dei);
		c.setUsers(l.getUsers());
		deleteLeaseById(id);
		contractService.saveContract(c);
	}
}
