package gr.hua.dit.dissys.entity.basicauth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<Auth, Integer>{
    Optional<Auth> findByUsername(String username);
}