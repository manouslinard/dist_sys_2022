package gr.hua.dit.dissys.payload.request;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String firstName;
    
	@Size(max = 30, message = "Name should not be greater than 30 characters")
	private String lastName;
	
	@Column(name = "afm", unique = true)
	@Size(min = 11, max = 11, message = "AFM should be exactly 11 digits")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid afm")
	private String afm;

	@Column(name = "phone")
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Please enter a valid phone number")
	private String phone;

	
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

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
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
    
}