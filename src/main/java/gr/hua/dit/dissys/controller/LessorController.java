package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.ContractRepository;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessor")
public class LessorController implements LessorContrInterface {

	@Autowired
	private LessorService lessorService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private LeaseRepository leaseRepo;
	
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
		return l.getLessorLeases();
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
	public void deleteLessorLease(@PathVariable String lessorUsername, @PathVariable int lid) {
		Lease lease = getLessorLease(lessorUsername, lid);
		leaseRepo.delete(lease);
	}

	@Override
	@GetMapping("/{lessorUsername}/assignTenantToLease/{tenantUsername}/{lid}")
	public boolean assignTenantToLease(@PathVariable String lessorUsername, @PathVariable String tenantUsername, @PathVariable int lid) {
		UserRegistration tenant = tenantService.findTenant(tenantUsername);
		List<Lease> leases = getAllLessorLeases(lessorUsername);
		for (Lease loop : leases) {
			if (loop.getId() == lid) {
				loop.setTenant(tenant);
				return true;
			}
		}
		return false;
	}

	@Override
	@PostMapping("/{lessorUsername}/leases/{lid}")
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

		leaseRepo.save(oldLease);
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
	@PostMapping("/{lessorUsername}/createLease")
	public Lease createLease(@Valid @RequestBody Lease lease, @PathVariable String lessorUsername) {

		UserRegistration l = lessorService.findLessor(lessorUsername);
		if (l == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		lease.setLessor(l);
		leaseRepo.save(lease);
		return lease;
	}

	@Override
	@PostMapping("/createTenant")
	public UserRegistration createTenant(@Valid @RequestBody UserRegistration tenant) {

		List<UserRegistration> tenantList = tenantService.getTenants();
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
		Long id = (long) 0;
		tenant.setId(id);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{lessorUsername}/contracts")
	public List<Contract> getAllLessorContracts(@PathVariable String lessorUsername) {
		UserRegistration lessor= lessorService.findLessor(lessorUsername);
		return lessor.getLessorContracts();
	}

	@Override
	@GetMapping("/{lessorUsername}/contracts/{cid}")
	public Contract getLessorContract(@PathVariable String lessorUsername, @PathVariable int cid) {
		UserRegistration lessor= get(lessorUsername);
		Contract contract= contractRepository.findById(cid).get();
		
		for (int i=0; i<lessor.getLessorContracts().size(); i++) {
			if (contract.getId() == cid) {
				return contract;
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
