package gr.hua.dit.dissys.controller;

import java.util.List;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.TenantAnswer;

public interface TenantContrInterface {

	public Lease getTenantLease(int id, int lid);

	public List<Lease> getAllTenantLeases(int id);

	public List<AverageUser> getAllLessors();

	public List<Contract> getAllTenantContracts(int id);

	public Contract getTenantContract(int id, int cid);
	
	//public void submitTenantAnswer(TenantAnswer tenantAnswer, int id, int lid);

	public AverageUser save(AverageUser tenant);

	public AverageUser get(int id);

	public void delete(int id);

}