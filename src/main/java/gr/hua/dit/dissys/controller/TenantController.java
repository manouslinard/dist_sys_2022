package gr.hua.dit.dissys.controller;


import gr.hua.dit.dissys.entity.TenantAnswer;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	

	@Override
	@GetMapping("/{tenantUsername}/leases/{lid}")
	public Lease getTenantLease(@PathVariable String tenantUsername, @PathVariable int lid) {
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
		UserRegistration tenant = (UserRegistration) tenantService.findTenant(tenantUsername);
		if (tenant == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
		return tenant.getTenantLeases();
	}

	@Override
	@GetMapping("/getAllLessors")
	public List<UserRegistration> getAllLessors() {
		return lessorService.getLessors();
	}

	@Override
	@GetMapping("/{tenantUsername}/contracts")
	public List<Contract> getAllTenantContracts(@PathVariable String tenantUsername) {
		// TODO: Chris
		UserRegistration tenant = new UserRegistration();
		tenant = tenantService.findTenant(tenantUsername);
		return tenant.getTenantContracts();
	}

	@Override
	@GetMapping("/{tenantUsername}/contracts/{cid}")
	public Contract getTenantContract(@PathVariable String tenantUsername, @PathVariable int cid) {
		UserRegistration tenant = tenantService.findTenant(tenantUsername);
		List<Contract> contracts =tenant.getTenantContracts();
		for(Contract loop:contracts){
			if(loop.getId()==cid){
				return loop;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}
	
	@Override
	@PostMapping("/{tenantUsername}/leases/{lid}/answer")
	public void submitTenantAnswer(@Valid @RequestBody TenantAnswer tenantAnswer, @PathVariable String tenantUsername, @PathVariable int lid) {
		// TODO: Chris
		Lease lease = new Lease();
		lease = tenantAnswer.getLease();
		lease.setTenantAnswer(tenantAnswer);
	}

	@Override
	@PostMapping("")
	public UserRegistration save(@Valid @RequestBody UserRegistration tenant) {
		Long id = (long) 0;
		tenant.setId(id);
		tenantService.saveTenant(tenant);
		return tenant;
	}

	@Override
	@GetMapping("/{tenantUsername}")
	public UserRegistration get(@PathVariable String tenantUsername) {
		return tenantService.findTenant(tenantUsername);
	}

	@Override
	@DeleteMapping("/{tenantUsername}")
	public void delete(@PathVariable String tenantUsername) {
		tenantService.deleteTenant(tenantUsername);
	}

}
