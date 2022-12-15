package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.entity.TenantAnswer;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.TenantRepository;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController implements TenantContrInterface {

	@Autowired
	private TenantService tenantService;

	@Autowired
	private LessorService lessorService;
	@Autowired
	private TenantRepository tenantRepository;
	@Autowired
	private LeaseRepository leaseRepository;

	@Override
	@GetMapping("/{id}/leases/{lid}")
	public Lease getTenantLease(@PathVariable int id, @PathVariable int lid) {
		List<Lease> leases = getAllTenantLeases(id);
		for (Lease lease : leases) {
			if (lease.getId() == lid) {
				return lease;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}

	@Override
	@GetMapping("/{id}/leases")
	public List<Lease> getAllTenantLeases(@PathVariable int id) {
		Tenant tenant = (Tenant) tenantService.findTenant(id);
		if (tenant == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return tenant.getLeases();
	}

	@Override
	@GetMapping("/getAllLessors")
	public List<Lessor> getAllLessors() {
		return lessorService.getLessors();
	}

	@Override
	@GetMapping("/{id}/contracts")
	public List<Contract> getAllTenantContracts(@PathVariable int id) {
		// TODO: Chris
		Tenant tenant = new Tenant();
		tenant = tenantService.findTenant(id);
		return tenant.getContracts();
	}

	@Override
	@GetMapping("/{id}/contracts/{cid}")
	public Contract getTenantContract(@PathVariable int id, @PathVariable int cid) {
		// TODO: Chris
		Tenant tenant = new Tenant();
		tenant = tenantService.findTenant(id);
		List<Contract> contracts =tenant.getContracts();
		for(Contract loop:contracts){
			if(loop.getId()==cid){
				return loop;
			}
		}
		return null;
	}
	
	@Override
	@PostMapping("/{id}/leases/{lid}/answer")
	public void submitTenantAnswer(@Valid @RequestBody TenantAnswer tenantAnswer) {
		// TODO: Chris
		Lease lease = new Lease();
		lease = tenantAnswer.getLease();
		lease.setTenantAnswer(tenantAnswer);
	}

	// TODO: check if needed:
	@Override
	@PostMapping("")
	public Tenant save(@Valid @RequestBody Tenant tenant) {
		tenant.setId(0);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{id}")
	public Tenant get(@PathVariable int id) {
		return tenantService.findTenant(id);
	}

	@Override
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		tenantService.deleteTenant(id);
	}

}
