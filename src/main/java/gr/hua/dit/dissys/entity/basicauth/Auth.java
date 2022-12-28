package gr.hua.dit.dissys.entity.basicauth;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "authorities", 
		uniqueConstraints = {
		        @UniqueConstraint(columnNames = { "username", "authority" })
		})
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@NotBlank
    @Size(max = 50)
    private String username;
    
	@NotBlank
    @Size(max = 50)
    private String authority;

	public Auth() {
		
	}
	
	public Auth(String username, String authority) {
		this.username = username;
		this.authority = authority;
	}    

    
}
