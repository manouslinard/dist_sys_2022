package gr.hua.dit.dissys.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tenant_answer")
public class TenantAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "tenant_com")
	private String tenantComment;

	@Column(name = "tenant_agree")
	private boolean hasAgreed;

	@OneToOne(mappedBy = "tenant_answer", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	private Lease lease;

	public TenantAnswer() {

	}

	public TenantAnswer(String tenantComment, boolean hasAgreed) {
		this.tenantComment = tenantComment;
		this.hasAgreed = hasAgreed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTenantComment() {
		return tenantComment;
	}

	public void setTenantComment(String tenantComment) {
		this.tenantComment = tenantComment;
	}

	public boolean isHasAgreed() {
		return hasAgreed;
	}

	public void setHasAgreed(boolean hasAgreed) {
		this.hasAgreed = hasAgreed;
	}

	public Lease getLease() {
		return lease;
	}

	public void setLease(Lease lease) {
		this.lease = lease;
	}

}
