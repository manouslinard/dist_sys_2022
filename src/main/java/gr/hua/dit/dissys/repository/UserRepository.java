package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.UserRegistration;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AverageUser, Integer> {
	   Optional<UserRegistration> findByUsername(String username);

	    Boolean existsByUsername(String username);

	    Boolean existsByEmail(String email);
}
