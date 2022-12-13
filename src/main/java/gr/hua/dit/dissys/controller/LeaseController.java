package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.dao.CourseDAO;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.LessorRepository;
import gr.hua.dit.dissys.repository.TenantRepository;
import gr.hua.dit.dissys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/leases")
public class LeaseController {

    @Autowired
    LeaseRepository leaseRepository;
    @Autowired
    private LessorRepository lessorRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping("")
    public List<Lease> getAll()
    {
        return leaseRepository.findAll();
    }

    @PostMapping("")
    Lease save(@Valid @RequestBody Lease lease) {
        lease.setId(0);
        leaseRepository.save(lease);
        return lease;
    }

    @GetMapping("/{id}")
    Lease get(@PathVariable int id) {
        return leaseRepository.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        leaseRepository.deleteById(id);
    }

    @PostMapping("/{cid}/lessor")
    Lessor addLessor(@PathVariable int cid, @RequestBody Lessor lessor) {
        int lessorId = lessor.getId();
        Lease lease = leaseRepository.findById(cid).get();

        if (lease == null) {
            throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        );
        }

        if (lessorId != 0) {
            Lessor alessor = lessorRepository.findById(lessorId).get();
            lease.setLessor(alessor);
            lessorRepository.save(alessor);
            return alessor;
        }

        lease.setLessor(lessor);
        lessorRepository.save(lessor);
        return lessor;

    }

    @PostMapping("/{cid}/tenant")
    Tenant addTenant(@PathVariable int cid, @RequestBody Tenant tenant) {
        int tenantId = tenant.getId();
        Lease lease = leaseRepository.findById(cid).get();

        if (lease == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        if (tenantId != 0) {
            Tenant atenant = tenantRepository.findById(tenantId).get();
            lease.setTenant(atenant);
            tenantRepository.save(atenant);
            return atenant;
        }

        lease.setTenant(tenant);
        tenantRepository.save(tenant);
        return tenant;

    }
}
