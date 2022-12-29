package gr.hua.dit.dissys.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "avg_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "afm")
        })
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
	@JsonIgnore
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

	@Column(name = "first_name")
	//@NotBlank(message = "Please enter the first name")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String firstName;

	@Column(name = "last_name")
	//@NotBlank(message = "Please enter the last name")
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String lastName;

	@JsonIgnore
	@Column(name = "afm", unique = true)
	//@NotBlank(message = "Please enter your AFM")
	@Size(min = 11, max = 11, message = "AFM should be exactly 11 digits")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid afm")
	private String afm;

	@JsonIgnore
	@Column(name = "phone")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid phone number")
	private String phone;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_leases",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "lease_id"))
	private List<Lease> userLeases;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "user_contracts",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "contract_id"))
	private List<Contract> userContracts;

	    
    public UserRegistration() {
    }

    public UserRegistration(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    public UserRegistration(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
		this.lastName = lastName;
    }
    
    public UserRegistration(String username, String email, String password, String firstName, String lastName, String afm, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.afm = afm;
		this.phone = phone;
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

	public List<Lease> getUserLeases() {
		return userLeases;
	}

	public void setUserLeases(List<Lease> userLeases) {
		this.userLeases = userLeases;
	}

	public List<Contract> getUserContracts() {
		return userContracts;
	}

	public void setUserContracts(List<Contract> userContracts) {
		this.userContracts = userContracts;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	@Override
	public String toString() {
		return "UserRegistration [id=" + id + ", username=" + username + ", email=" + email + ", roles=" + roles + "]";
	}

    
}