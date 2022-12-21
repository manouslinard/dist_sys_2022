package gr.hua.dit.dissys.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.payload.request.TenantAnswer;
import gr.hua.dit.dissys.payload.response.MessageResponse;
import gr.hua.dit.dissys.entity.UserRegistration;

public interface TenantContrInterface {

	public Lease getTenantLease(String tenantUsername, int lid);

	public List<Lease> getAllTenantLeases(String tenantUsername);

	public List<UserRegistration> getAllLessors();

	public List<Contract> getAllTenantContracts(String tenantUsername);

	public Contract getTenantContract(String tenantUsername, int cid);
	
	public ResponseEntity<MessageResponse> submitTenantAnswer(TenantAnswer tenantAnswer, String tenantUsername, int lid);

	public UserRegistration get(String tenantUsername);

	public void delete(String tenantUsername);

}
