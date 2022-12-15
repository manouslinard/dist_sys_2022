package gr.hua.dit.dissys.controller;

import java.util.List;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.Tenant;

public interface LessorContrInterface {

	public List<Tenant> getAllTenants();

	public List<Lessor> getAllLessors();
	
	public List<Lease> getAllLessorLeases(int id);

	public Lease getLessorLease(int id, int lid);

	public void deleteLessorLease(int id, int lid);

	public boolean assignTenantToLease(int tid, int lid, int id);

	public void updateLease(Lease lease, int id, int lid);

	public void createLease(Lease lease, int id);

	public Tenant createTenant(Tenant tenant);

	public List<Contract> getAllLessorContracts( int id);

	public Contract getLessorContract( int id,  int cid);
	
	public Lessor save(Lessor lessor);

	public Lessor get(int id);

	public void delete(int id);

}
