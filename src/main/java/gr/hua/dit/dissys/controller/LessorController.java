package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
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
public class LessorController {

    @Autowired
    private LessorService lessorService;
    
    @Autowired
    private TenantService tenantService;

    @Autowired
    private LeaseRepository leaseRepo;
    
    @GetMapping("/getAllTenants")
    public List<Tenant> getAllTenants()
    {
    	return tenantService.getTenants();
    }

    @GetMapping("/{id}/leases")
    public List<Lease> getAllLeases(@PathVariable int id) {
        Lessor l = (Lessor) lessorService.findLessor(id);
        if (l == null) {
        	throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
             );
        }
        return l.getLeases();
    }
   
    @GetMapping("/{id}/leases/{lid}")
    public Lease getLessorLease(@PathVariable int id, @PathVariable int lid) {
        List<Lease> lessorLeases = getAllLeases(id);
        for(Lease lease : lessorLeases) {
        	if(lease.getId() == lid) {
        		return lease;
        	}
        }
       	throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }
    
    @DeleteMapping("{id}/leases/{lid}")
    public void deleteLease(@PathVariable int id, @PathVariable int lid) {
    	Lease lease = getLessorLease(id, lid);
    	leaseRepo.delete(lease);
    }
    
    
    
    @PostMapping("")
    public Lessor save(@Valid @RequestBody Lessor lessor) {
        lessor.setId(0);
        lessorService.saveLessor(lessor);
        return lessor;
    }

    @GetMapping("/{id}")
    public Lessor get(@PathVariable int id) {
        return lessorService.findLessor(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        lessorService.deleteLessor(id);
    }
}



