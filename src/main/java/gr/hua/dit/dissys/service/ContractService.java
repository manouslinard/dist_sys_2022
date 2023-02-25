package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.Contract;

public interface ContractService {

    public List<Contract> getContracts();
    public void saveContract(Contract contract);

    public Contract findContract(int id);

    public void deleteContract(int id);
    
	public Contract findContractByTitle(String contractTitle);
	
	
}
