package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;
import gr.hua.dit.dissys.repository.ContractRepository;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.LessorRepository;
import gr.hua.dit.dissys.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/contracts")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;

    @GetMapping("")
    public List<Contract> getAll()
    {
        return contractRepository.findAll();
    }

    @PostMapping("")
    public Contract save(@Valid @RequestBody Contract contract) {
        contract.setId(0);
        contractRepository.save(contract);
        return contract;
    }

    @GetMapping("/{id}")
    public Contract get(@PathVariable int id) {
        return contractRepository.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        contractRepository.deleteById(id);
    }

}
