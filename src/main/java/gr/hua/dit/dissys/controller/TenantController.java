package gr.hua.dit.dissys.controller;


import gr.hua.dit.dissys.payload.request.LoginRequest;
import gr.hua.dit.dissys.payload.request.TenantAnswer;
import gr.hua.dit.dissys.payload.response.MessageResponse;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.service.AdminService;
import gr.hua.dit.dissys.service.ContractService;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantController implements TenantContrInterface {

	@Autowired
	private TenantService tenantService;

	@Autowired
	private LessorService lessorService;
	

	@Autowired
	private LeaseService leaseService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private AdminService adminService;
	
	private boolean isAdmin(String username) {
		List <UserRegistration> admins = adminService.getAdmins();
		for (UserRegistration a: admins) {
			if(a.getUsername().equals(username))
				return true;
		}
		return false;
	}

	private void isTenantAdmin(String lessorUsername, String error_msg) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String logged_in_username = ((LoginRequest)principal).getUsername();
		if (!isAdmin(logged_in_username) && !lessorUsername.equals(logged_in_username)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, error_msg);         	
		}		
	}
	
	
	@Override
	@GetMapping("/{tenantUsername}/leases/{lid}")
	public Lease getTenantLease(@PathVariable String tenantUsername, @PathVariable int lid) {
		isTenantAdmin(tenantUsername, "Cannot access leases of not logged in tenant!");
		List<Lease> leases = getAllTenantLeases(tenantUsername);
		for (Lease lease : leases) {
			if (lease.getId() == lid) {
				return lease;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	@Override
	@GetMapping("/{tenantUsername}/leases")
	public List<Lease> getAllTenantLeases(@PathVariable String tenantUsername) {
		isTenantAdmin(tenantUsername, "Cannot access leases of not logged in tenant!");
		UserRegistration tenant = (UserRegistration) tenantService.findTenant(tenantUsername);
		if (tenant == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return tenant.getUserLeases();
	}

	@Override
	@GetMapping("/getAllLessors")
	public List<UserRegistration> getAllLessors() {
		return lessorService.getLessors();
	}

	@Override
	@GetMapping("/{tenantUsername}/contracts")
	public List<Contract> getAllTenantContracts(@PathVariable String tenantUsername) {
		isTenantAdmin(tenantUsername, "Cannot access contracts of not logged in tenant!");
		UserRegistration tenant = tenantService.findTenant(tenantUsername);
		return tenant.getUserContracts();
	}

	@Override
	@GetMapping("/{tenantUsername}/contracts/{cid}")
	public Contract getTenantContract(@PathVariable String tenantUsername, @PathVariable int cid) {
		isTenantAdmin(tenantUsername, "Cannot access contracts of not logged in tenant!");
		UserRegistration tenant = tenantService.findTenant(tenantUsername);
		List<Contract> contracts =tenant.getUserContracts();
		for(Contract loop:contracts){
			if(loop.getId()==cid){
				return loop;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}
	
	@Override
	@PostMapping("/{tenantUsername}/leases/{lid}/answer")
	public ResponseEntity<MessageResponse> submitTenantAnswer(@Valid @RequestBody TenantAnswer tenantAnswer, @PathVariable String tenantUsername, @PathVariable int lid) {
		isTenantAdmin(tenantUsername, "Cannot answer leases of not logged in tenant!");
		Lease lease = getTenantLease(tenantUsername, lid);
		lease.setTenantAgree(tenantAnswer.getHasAgreed());
		lease.setTenantCom(tenantAnswer.getTenantComment());
		if(lease.isTenantAgree()) {
			Contract contract = new Contract(lease.getTitle(), lease.getAddress(), lease.getTk(), lease.getDimos(), lease.getReason(), lease.getCost(), lease.getStartDate(),
					lease.getEndDate(), lease.getSp_con(), lease.getDei());
			UserRegistration tenant = tenantService.findTenant(tenantUsername);
			tenant.getUserContracts().add(contract);
			List<UserRegistration> users = lease.getUsers();
			//Users are only 2: Tenant and Lessor
			for(UserRegistration user:users){
				if(!user.getUsername().equals(tenantUsername)){
					user.getUserContracts().add(contract);
				}
			}
			List<UserRegistration> leaseUsers = lease.getUsers();
			// removes lease from lessor and tenant:
			for (UserRegistration u: leaseUsers) {
				u.getUserLeases().remove(lease);
			}
			leaseService.deleteLease(lease);
		} else {
			// saves lease if not agreed:
			leaseService.saveLease(lease);
		}
		return ResponseEntity.ok(new MessageResponse("Answer submitted."));
	}

	@Override
	@GetMapping("/{tenantUsername}")
	public UserRegistration get(@PathVariable String tenantUsername) {
		isTenantAdmin(tenantUsername, "Cannot view info of not logged in tenant!");
		return tenantService.findTenant(tenantUsername);
	}

	@Override
	@DeleteMapping("/{tenantUsername}")
	public ResponseEntity<MessageResponse> delete(@PathVariable String tenantUsername) {
		isTenantAdmin(tenantUsername, "Cannot delete not logged in tenant!");
		tenantService.deleteTenant(tenantUsername);
		return ResponseEntity.ok(new MessageResponse("Requested tenant deleted."));
	}

}
