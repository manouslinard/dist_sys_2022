package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
}
