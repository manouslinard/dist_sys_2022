package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    TenantService tenantService;

    @GetMapping("")
    public List<Tenant> getAll()
    {
        return tenantService.getTenants();
    }

    @PostMapping("")
    Tenant save(@Valid @RequestBody Tenant tenant) {
        tenant.setId(0);
        tenantService.saveTenant(tenant);
        return tenant;
    }

    @GetMapping("/{id}")
    Tenant get(@PathVariable int id) {
        return tenantService.findTenant(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tenantService.deleteTenant(id);
    }
}
