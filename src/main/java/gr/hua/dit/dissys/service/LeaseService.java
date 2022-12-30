package gr.hua.dit.dissys.service;

import java.util.List;

import gr.hua.dit.dissys.entity.Lease;

public interface LeaseService {

    public List<Lease> getLeases();
    public void saveLease(Lease lease);

    public Lease findLeaseByTitle(String leaseTitle);

    public Lease findLease(int id);

    public void deleteLease(int id);
	
}
