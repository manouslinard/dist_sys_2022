package gr.hua.dit.dissys.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "contract")
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	@NotBlank(message = "Please enter the lease's title")
	private String title;

	@Column(name = "address")
	@NotBlank(message = "Please enter the address")
	private String address;

	@Column(name = "tk")
	@NotBlank(message = "Please enter your postal code")
	@Size(min = 5, max = 5, message = "Postal code should be exactly 5 digits")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid postal code")
	private String tk;

	@Column(name = "dimos")
	@NotBlank(message = "Please enter your municipality")
	private String dimos;

	@Column(name = "reason")
	private String reason;

	@Column(name = "cost")
	@NotBlank(message = "Please enter the cost")
	private double cost;

	@Column(name = "start_date")
	@NotBlank(message = "Please enter the contract's start date")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String startDate;

	@Column(name = "end_date")
	@NotBlank(message = "Please enter the last name")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String endDate;

	// special conditions:
	@Column(name = "sp_con")
	private String sp_con;

	@Column(name = "dei")
	@NotBlank(message = "Please enter DEI account number")
	private String dei;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "lessor_id")
	@JsonBackReference
	private Lessor lessor;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "tenant_id")
	@JsonBackReference
	private Tenant tenant;

	public Contract() {

	}

	public Contract(String title, String address, String tk, String dimos, String reason, double cost, String startDate,
			String endDate, String sp_con, String dei) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Lessor getLessor() {
		return lessor;
	}

	public void setLessor(Lessor lessor) {
		this.lessor = lessor;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
	public String toString() {
		return "Contract [id=" + id + "]";
	}
}
