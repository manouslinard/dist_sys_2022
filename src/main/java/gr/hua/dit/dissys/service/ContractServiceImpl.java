package gr.hua.dit.dissys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.repository.ContractRepository;

import javax.transaction.Transactional;

@Service
public class ContractServiceImpl implements ContractService{

	@Autowired
    private ContractRepository contractRepository;
	
	@Override
	@Transactional
	public List<Contract> getContracts() {
		return contractRepository.findAll();
	}

	@Override
	@Transactional
	public void saveContract(Contract contract) {
        contractRepository.save(contract);						
	}

	@Override
	@Transactional
	public Contract findContract(int id) {
		return contractRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteContract(int id) {
        contractRepository.deleteById(id);		
	}

}
