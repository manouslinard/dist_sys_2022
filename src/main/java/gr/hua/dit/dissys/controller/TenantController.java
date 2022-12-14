package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Tenant;
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
		return null;
	}

	@Override
	@GetMapping("/{id}/contracts/{cid}")
	public Contract getTenantContract(@PathVariable int id, @PathVariable int cid) {
		// TODO: Chris
		return null;
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
