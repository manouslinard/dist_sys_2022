package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;
import gr.hua.dit.dissys.repository.ContractRepository;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessor")
public class LessorController implements LessorContrInterface {

	@Autowired
	private LessorService lessorService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private LeaseService leaseService;
	
    @Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private ContractRepository contractRepository;

	@Override
	@GetMapping("/getAllTenants")
	public List<UserRegistration> getAllTenants() {
		return tenantService.getTenants();
	}

	@Override
	@GetMapping("/{lessorUsername}/leases")
	public List<Lease> getAllLessorLeases(@PathVariable String lessorUsername) {
		UserRegistration l = (UserRegistration) lessorService.findLessor(lessorUsername);
		if (l == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return l.getUserLeases();
	}

	@Override
	@GetMapping("/{lessorUsername}/leases/{lid}")
	public Lease getLessorLease(@PathVariable String lessorUsername, @PathVariable int lid) {
		List<Lease> lessorLeases = getAllLessorLeases(lessorUsername);
		for (Lease lease : lessorLeases) {
			if (lease.getId() == lid) {
				return lease;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	@Override
	@DeleteMapping("{lessorUsername}/leases/{lid}")
	public ResponseEntity<MessageResponse> deleteLessorLease(@PathVariable String lessorUsername, @PathVariable int lid) {
		Lease lease = getLessorLease(lessorUsername, lid);
		List <UserRegistration> leaseUsers = lease.getUsers();
		// removes lease from users (removes all references):
		for (UserRegistration u: leaseUsers) {
			u.getUserLeases().remove(lease);
		}
		// removes it from db:
		leaseService.deleteLease(lease);
		return ResponseEntity.ok(new MessageResponse("Requested lease has been deleted."));
	}

//	@Override
//	@PostMapping("/{lessorUsername}/assign/{tenantUsername}/{lid}")
//	public Lease assignTenantToLease(@PathVariable String lessorUsername, @PathVariable String tenantUsername, @PathVariable int lid) {
//		UserRegistration tenant = tenantService.findTenant(tenantUsername);
//		List<Lease> leases = getAllLessorLeases(lessorUsername);
//		for (Lease lease : leases) {
//			if (lease.getId() == lid) {
//				if(!tenant.getUserLeases().contains(lease)) {
//					tenant.getUserLeases().add(lease);
//					tenantService.saveTenant(tenant);
//				}
//				return lease;
//			}
//		}
//		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
//	}

	@Override
	@PutMapping("/{lessorUsername}/leases/{lid}")
	public Lease updateLease(@Valid @RequestBody Lease lease, @PathVariable String lessorUsername, @PathVariable int lid) {

		Lease oldLease = getLessorLease(lessorUsername, lid);

		if (!checkNullEmptyBlank(lease.getAddress())) {
			oldLease.setAddress(lease.getAddress());
		}
		if (!checkNullEmptyBlank(lease.getDei())) {
			oldLease.setDei(lease.getDei());
		}
		if (!checkNullEmptyBlank(lease.getDimos())) {
			oldLease.setDimos(lease.getDimos());
		}
		if (!checkNullEmptyBlank(lease.getEndDate())) {
			oldLease.setEndDate(lease.getEndDate());
		}
		if (!checkNullEmptyBlank(lease.getStartDate())) {
			oldLease.setStartDate(lease.getStartDate());
		}
		if (!checkNullEmptyBlank(lease.getReason())) {
			oldLease.setReason(lease.getReason());
		}
		if (!checkNullEmptyBlank(lease.getSp_con())) {
			oldLease.setSp_con(lease.getSp_con());
		}
		if (!checkNullEmptyBlank(lease.getTitle())) {
			oldLease.setTitle(lease.getTitle());
		}
		if (!checkNullEmptyBlank(lease.getTk())) {
			oldLease.setTk(lease.getTk());
		}
		if (!checkNullEmptyBlank(String.valueOf(lease.getCost()))) {
			oldLease.setCost(lease.getCost());
		}

		leaseService.saveLease(oldLease);
		return oldLease;

	}

	private boolean checkNullEmptyBlank(String strToCheck) {
		// check whether the given string is null or empty or blank
		if (strToCheck == null || strToCheck.isEmpty() || strToCheck.isBlank()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@PostMapping("/{lessorUsername}/{tenantUsername}/createLease")
	public Lease createLease(@Valid @RequestBody Lease lease, @PathVariable String lessorUsername, @PathVariable String tenantUsername) {

		UserRegistration l = lessorService.findLessor(lessorUsername);
		if (l == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		
		UserRegistration t = tenantService.findTenant(tenantUsername);
		if (t == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		
		// sets tenant asnwer to def (in case lessor submits them):

		lease.setTenantAgree(false);
		lease.setTenantCom(null);
		lease.setUsers(new ArrayList<UserRegistration>());
		lease.getUsers().add(l);
		lease.getUsers().add(t);
		leaseService.saveLease(lease);
		return lease;
	}

	@Override
	@PostMapping("/createTenant")
	public UserRegistration createTenant(@Valid @RequestBody UserRegistration tenant) {

		List<UserRegistration> tenantList = tenantService.getTenants();

		// updates old Tenant:
		for (UserRegistration oldTenant : tenantList) {
			if (oldTenant.getEmail().equals(tenant.getEmail())) {
				if (!checkNullEmptyBlank(tenant.getAfm())) {
					oldTenant.setAfm(tenant.getAfm());
				}
				if(!checkNullEmptyBlank(tenant.getFirstName())) {
					oldTenant.setFirstName(tenant.getFirstName());
				}
				if(!checkNullEmptyBlank(tenant.getLastName())) {
					oldTenant.setLastName(tenant.getLastName());
				}
				if(!checkNullEmptyBlank(tenant.getPhone())) {
					oldTenant.setPhone(tenant.getPhone());
				}
				tenantService.saveTenant(oldTenant);
				return oldTenant;
			}
		}
		
		// creates new tenant
		Long id = (long) 0;
		tenant.setId(id);
		Role role = roleRepository.findByName(ERole.ROLE_TENANT).get();
		tenant.getRoles().add(role);
		// def null (in case lessor enters them):
		tenant.setUserLeases(null);
		tenant.setUserContracts(null);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{lessorUsername}/contracts")
	public List<Contract> getAllLessorContracts(@PathVariable String lessorUsername) {
		UserRegistration lessor= lessorService.findLessor(lessorUsername);
		return lessor.getUserContracts();
	}

	@Override
	@GetMapping("/{lessorUsername}/contracts/{cid}")
	public Contract getLessorContract(@PathVariable String lessorUsername, @PathVariable int cid) {
		UserRegistration lessor= lessorService.findLessor(lessorUsername);
		List<Contract> contracts= lessor.getUserContracts();
		
		for (Contract c: contracts) {
			if (c.getId() == cid) {
				return c;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	@Override
	@PostMapping("")
	public UserRegistration save(@Valid @RequestBody UserRegistration lessor) {
		Long id = (long) 0;
		lessor.setId(id);
		lessorService.saveLessor(lessor);
		return lessor;
	}

	@Override
	@GetMapping("/{lessorUsername}")
	public UserRegistration get(@PathVariable String lessorUsername) {
		return lessorService.findLessor(lessorUsername);
	}

	@Override
	@DeleteMapping("/{lessorUsername}")
	public void delete(@PathVariable String lessorUsername) {
		lessorService.deleteLessor(lessorUsername);
	}

	@Override
	@GetMapping("/getAllLessors")
	public List<UserRegistration> getAllLessors() {
		return lessorService.getLessors();
	}

}
