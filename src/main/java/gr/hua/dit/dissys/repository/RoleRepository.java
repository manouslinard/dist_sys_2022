package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}