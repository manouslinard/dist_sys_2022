package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    TenantRepository tenantRepository;

    @GetMapping("")
    public List<Tenant> getAll()
    {
        return tenantRepository.findAll();
    }

    @PostMapping("")
    Tenant save(@Valid @RequestBody Tenant tenant) {
        tenant.setId(0);
        tenantRepository.save(tenant);
        return tenant;
    }

    @GetMapping("/{id}")
    Tenant get(@PathVariable int id) {
        return tenantRepository.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tenantRepository.deleteById(id);
    }
}
