package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Integer> {
}
