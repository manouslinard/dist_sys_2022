package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.AverageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AverageUser, Integer> {
}
