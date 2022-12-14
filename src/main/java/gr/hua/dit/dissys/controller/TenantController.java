package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import gr.hua.dit.dissys.entity.Lease;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("")
    public List<Tenant> getAll()
    {
        return tenantService.getTenants();
    }

    @PostMapping("")
    public Tenant save(@Valid @RequestBody Tenant tenant) {
        tenant.setId(0);
        tenantService.saveTenant(tenant);
        return tenant;
    }
    @GetMapping("/{id}/leases/{lid}")
    public Lease getTenantsLeaseById(@PathVariable int id,@PathVariable int lid){
        List<Lease> leases = getTenantLeasesById(id);
        for(Lease lease:leases){
            if(lease.getId()==lid){
                return lease;
            }
        }
        return null;
    }

    @GetMapping("/{id}/leases")
    public List<Lease> getTenantLeasesById(@PathVariable int id)
    {
        Tenant tenant = (Tenant) tenantService.findTenant(id);
        if (tenant == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return tenant.getLeases();
    }

    @GetMapping("/{id}")
    public Tenant get(@PathVariable int id) {
        return tenantService.findTenant(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tenantService.deleteTenant(id);
    }
}
