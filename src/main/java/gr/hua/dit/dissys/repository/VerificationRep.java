package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.VerificationCode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRep extends JpaRepository<VerificationCode,Integer> {
   VerificationCode findByVerificationCode(String verificationCode);
   List<VerificationCode> findByEmail(String email);
}
