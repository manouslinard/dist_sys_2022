package gr.hua.dit.dissys.payload.request;

import javax.validation.constraints.Size;

public class TenantAnswer {

	@Size(max = 100)
	private String tenantComment;

	private boolean hasAgreed;

	public String getTenantComment() {
		return tenantComment;
	}

	public void setTenantComment(String tenantComment) {
		this.tenantComment = tenantComment;
	}

	public boolean getHasAgreed() {
		return hasAgreed;
	}

	public void setHasAgreed(boolean hasAgreed) {
		this.hasAgreed = hasAgreed;
	}

}
