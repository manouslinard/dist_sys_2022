package gr.hua.dit.dissys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "lessor")
public class Lessor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	@NotBlank(message = "Please enter the first name")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String firstName;

	@Column(name = "last_name")
	@NotBlank(message = "Please enter the last name")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String lastName;

	@Column(name = "email", unique = true)
	@Email(message = "Please enter a valid email")
	@NotBlank(message = "Please enter your email")
	@Size(max = 50)
	private String email;

	@JsonIgnore
	@Column(name = "afm", unique = true)
	@NotBlank(message = "Please enter your AFM")
	@Size(min = 11, max = 11, message = "AFM should be exactly 11 digits")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid afm")
	private String afm;

	@JsonIgnore
	@Column(name = "phone")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid phone number")
	private String phone;

	@JsonIgnore
	@OneToMany(mappedBy = "lessor", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Lease> leases;

	@JsonIgnore
	@OneToMany(mappedBy = "lessor", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JsonManagedReference
	private List<Contract> contracts;

	public Lessor() {

	}

	public Lessor(String firstName, String lastName, String email, String afm, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.afm = afm;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Lease> getLeases() {
		return leases;
	}

	public void setLeases(List<Lease> leases) {
		this.leases = leases;
	}

	public String getAfm() {
		return afm;
	}

	public void setAfm(String afm) {
		this.afm = afm;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	/*
	 * // add convenience methods for bi-directional relation public void add(Lease
	 * alease) { if(leases == null) { leases = new ArrayList<>(); }
	 * leases.add(alease); alease.setLessor(this); }
	 * 
	 * // add convenience methods for bi-directional relation public void
	 * addContract(Contract contract) { if(contracts == null) { contracts = new
	 * ArrayList<>(); } contracts.add(contract); contract.setLessor(this); }
	 * 
	 */

	@Override
	public String toString() {
		return "Lessor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
