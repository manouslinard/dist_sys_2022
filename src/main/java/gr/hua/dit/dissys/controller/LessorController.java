package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
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

	@GetMapping("/getAllTenants")
	public List<Tenant> getAllTenants() {
		return tenantService.getTenants();
	}

	@Override
	@GetMapping("/{id}/leases")
	public List<Lease> getAllLessorLeases(@PathVariable int id) {
		Lessor l = (Lessor) lessorService.findLessor(id);
		if (l == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return l.getLeases();
	}

	@Override
	@GetMapping("/{id}/leases/{lid}")
	public Lease getLessorLease(@PathVariable int id, @PathVariable int lid) {
		List<Lease> lessorLeases = getAllLessorLeases(id);
		for (Lease lease : lessorLeases) {
			if (lease.getId() == lid) {
				return lease;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	@Override
	@DeleteMapping("{id}/leases/{lid}")
	public void deleteLessorLease(@PathVariable int id, @PathVariable int lid) {
		Lease lease = getLessorLease(id, lid);
		leaseRepo.delete(lease);
	}

	@Override
	@GetMapping("/{id}/assignTenantToLease/{tid}/{lid}")
	public boolean assignTenantToLease(@PathVariable int tid, @PathVariable int lid, @PathVariable int id) {
		Tenant tenant = tenantService.findTenant(tid);
		List<Lease> leases = getAllLessorLeases(id);
		for (Lease loop : leases) {
			if (loop.getId() == lid) {
				loop.setTenant(tenant);
				return true;
			}
		}
		return false;
	}

	@Override
	@PostMapping("/{id}/leases/{lid}")
	public void updateLease(@Valid @RequestBody Lease lease, @PathVariable int id, @PathVariable int lid) {

		Lease oldLease = getLessorLease(id, lid);

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
	@PostMapping("/{id}/createLease")
	public void createLease(@Valid @RequestBody Lease lease, @PathVariable int id) {

		Lessor l = lessorService.findLessor(id);
		if (l == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		lease.setLessor(l);
		leaseRepo.save(lease);
	}

	@Override
	@PostMapping("/createTenant")
	public Tenant createTenant(@Valid @RequestBody Tenant tenant) {

		List<Tenant> tenantList = tenantService.getTenants();
		for (Tenant oldTenant : tenantList) {
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
		tenant.setId(0);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{id}/contracts")
	public List<Contract> getAllLessorContracts(@PathVariable int id) {
		Lessor lessor= lessorService.findLessor(id);
		return lessor.getContracts();
	}

	@Override
	@GetMapping("/{id}/contracts/{cid}")
	public Contract getLessorContract(@PathVariable int id, @PathVariable int cid) {
		Lessor lessor= get(id);
		Contract contract= contractRepository.findById(cid).get();
		
		for (int i=0; i<lessor.getContracts().size(); i++) {
			if (contract.getId() == cid) {
				return contract;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	// TODO: check if needed:
	@Override
	@PostMapping("")
	public Lessor save(@Valid @RequestBody Lessor lessor) {
		lessor.setId(0);
		lessorService.saveLessor(lessor);
		return lessor;
	}

	@Override
	@GetMapping("/{id}")
	public Lessor get(@PathVariable int id) {
		return lessorService.findLessor(id);
	}

	@Override
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		lessorService.deleteLessor(id);
	}

}
