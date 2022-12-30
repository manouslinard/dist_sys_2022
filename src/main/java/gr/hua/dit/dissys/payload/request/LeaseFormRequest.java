package gr.hua.dit.dissys.payload.request;

import gr.hua.dit.dissys.entity.AverageUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class LeaseFormRequest {

	private String title;

	private int lease_id;

	// @NotBlank(message = "Please enter the address")
	private String address;

	private String tk;

	private String dimos;

	private String reason;

	// @NotBlank(message = "Please enter the cost")
	private double cost;

	private String startDate;

	private String endDate;

	// special conditions:

	private String sp_con;

	private String dei;

	private String tenant_username;

	private String lessor_username;
	
	private String tenant_com;

	private String new_title;


	public LeaseFormRequest() {

	}

	public LeaseFormRequest(int lease_id) {
		this.lease_id = lease_id;
	}

	public LeaseFormRequest(String title, String address, String tk, String dimos, String reason, double cost,
			String startDate, String endDate, String sp_con, String dei) {
		this.title = title;
		this.address = address;
		this.tk = tk;
		this.dimos = dimos;
		this.reason = reason;
		this.cost = cost;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sp_con = sp_con;
		this.dei = dei;
	}

	public LeaseFormRequest(String title, String address, String tk, String dimos, String reason, double cost,
			String startDate, String endDate, String sp_con, String dei, String tenantUsername, String lessorUsername) {
		this.title = title;
		this.address = address;
		this.tk = tk;
		this.dimos = dimos;
		this.reason = reason;
		this.cost = cost;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sp_con = sp_con;
		this.dei = dei;
		this.tenant_username = tenantUsername;
		this.lessor_username = lessorUsername;
	}
	
	public String getTenant_username() {
		return tenant_username;
	}

	public void setTenant_username(String tenant_username) {
		this.tenant_username = tenant_username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTk() {
		return tk;
	}

	
	
	public String getLessor_username() {
		return lessor_username;
	}

	public void setLessor_username(String lessor_username) {
		this.lessor_username = lessor_username;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getDimos() {
		return dimos;
	}

	public void setDimos(String dimos) {
		this.dimos = dimos;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSp_con() {
		return sp_con;
	}

	public void setSp_con(String sp_con) {
		this.sp_con = sp_con;
	}

	public String getDei() {
		return dei;
	}

	public void setDei(String dei) {
		this.dei = dei;
	}

	public String getTenant_com() {
		return tenant_com;
	}

	public void setTenant_com(String tenant_com) {
		this.tenant_com = tenant_com;
	}

	
	
	public int getLease_id() {
		return lease_id;
	}

	public void setLease_id(int lease_id) {
		this.lease_id = lease_id;
	}

	public String getNew_title() {
		return new_title;
	}

	public void setNew_title(String new_title) {
		this.new_title = new_title;
	}

	// define toString
	@Override
	public String toString() {
		return "Lease Form [title=" + title + "]";
	}

}
