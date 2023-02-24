package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRep extends JpaRepository<VerificationCode,Integer> {

}
