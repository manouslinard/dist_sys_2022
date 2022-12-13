package gr.hua.dit.dissys.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="contract")
public class Contract {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne(mappedBy="contract", cascade= CascadeType.ALL)
    private Lease lease;
    
    public Contract() {

    }
    
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public Lease getLease() {
		return lease;
	}

	public void setLease(Lease lease) {
		this.lease = lease;
	}

	@Override
    public String toString() {
        return "Contract [id=" + id + "]";
    }
}
