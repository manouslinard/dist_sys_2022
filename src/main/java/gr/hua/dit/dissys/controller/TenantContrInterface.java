package gr.hua.dit.dissys.controller;

import java.util.List;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;

public interface TenantContrInterface {

	public Lease getTenantLease(int id, int lid);

	public List<Lease> getAllTenantLeases(int id);

	public List<Lessor> getAllLessors();

	public List<Contract> getAllTenantContracts(int id);

	public Contract getTenantContract(int id, int cid);

	// TODO: check if needed:
	public Tenant save(Tenant tenant);

	public Tenant get(int id);

	public void delete(int id);

	// TODO: submitTenantAnswer
}
