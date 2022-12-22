package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserRegistration, Long> {
    Optional<UserRegistration> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}