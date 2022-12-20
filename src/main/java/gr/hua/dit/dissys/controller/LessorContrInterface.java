package gr.hua.dit.dissys.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.response.MessageResponse;

public interface LessorContrInterface {

	public List<UserRegistration> getAllTenants();

	public List<UserRegistration> getAllLessors();
	
	public List<Lease> getAllLessorLeases(String lessorUsername);

	public Lease getLessorLease(String lessorUsername, int lid);

	public ResponseEntity<MessageResponse> deleteLessorLease(String lessorUsername, int lid);

	public Lease assignTenantToLease(String lessorUsername, String tenantUsername, int lid);

	public Lease updateLease(Lease lease, String lessorUsername, int lid);

	public Lease createLease(Lease lease, String lessorUsername);

	public UserRegistration createTenant(UserRegistration tenant);

	public List<Contract> getAllLessorContracts(String lessorUsername);

	public Contract getLessorContract(String lessorUsername,  int cid);
	
	public UserRegistration save(UserRegistration lessor);

	public UserRegistration get(String lessorUsername);

	public void delete(String lessorUsername);

}
