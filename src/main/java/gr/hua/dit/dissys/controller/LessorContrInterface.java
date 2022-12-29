package gr.hua.dit.dissys.controller;

import java.util.List;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.AverageUser;

public interface LessorContrInterface {

	public List<AverageUser> getAllTenants();

	public List<AverageUser> getAllLessors();
	
	public List<Lease> getAllLessorLeases(int id);

	public Lease getLessorLease(int id, int lid);

	public void deleteLessorLease(int id, int lid);

	//public boolean assignTenantToLease(int tid, int lid, int id);

	public void updateLease(Lease lease, int id, int lid);

	//public void createLease(Lease lease, int id);

	public AverageUser createTenant(AverageUser tenant);

	public List<Contract> getAllLessorContracts( int id);

	public Contract getLessorContract( int id,  int cid);
	
	public AverageUser save(AverageUser lessor);

	public AverageUser get(int id);

}
