package gr.hua.dit.dissys.entity.basicauth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(	name = "users" )
public class User {
    @Id
    @Size(max = 50)
    private String username;
    
    @NotBlank
	@JsonIgnore
    @Size(max = 100)
    private String password;

    @Column
    private boolean enabled;

    public User() {
    	
    }
    
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.enabled = true;
	}

    
}
