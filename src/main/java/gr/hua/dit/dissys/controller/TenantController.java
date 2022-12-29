package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.AverageUser;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tenant")
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
		AverageUser tenant = (AverageUser) tenantService.findTenantById(id);
		if (tenant == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return tenant.getUserLeases();
	}

	@Override
	@GetMapping("/getAllLessors")
	public List<AverageUser> getAllLessors() {
		return lessorService.getLessors();
	}

	@Override
	@GetMapping("/{id}/contracts")
	public List<Contract> getAllTenantContracts(@PathVariable int id) {
		// TODO: Chris
		AverageUser tenant = new AverageUser();
		tenant = tenantService.findTenantById(id);
		return tenant.getUserContracts();
	}

	@Override
	@GetMapping("/{id}/contracts/{cid}")
	public Contract getTenantContract(@PathVariable int id, @PathVariable int cid) {
		AverageUser tenant = tenantService.findTenantById(id);
		List<Contract> contracts =tenant.getUserContracts();
		for(Contract loop:contracts){
			if(loop.getId()==cid){
				return loop;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}
	
//	@Override
//	@PostMapping("/{id}/leases/{lid}/answer")
//	public void submitTenantAnswer(@Valid @RequestBody TenantAnswer tenantAnswer, @PathVariable int id, @PathVariable int lid) {
//		// TODO: Chris
//		Lease lease = new Lease();
//		lease = tenantAnswer.getLease();
//		lease.setTenantAnswer(tenantAnswer);
//	}

	@Override
	@PostMapping("")
	public AverageUser save(@Valid @RequestBody AverageUser tenant) {
		tenant.setId(0);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{id}")
	public AverageUser get(@PathVariable int id) {
		return tenantService.findTenantById(id);
	}
	
}