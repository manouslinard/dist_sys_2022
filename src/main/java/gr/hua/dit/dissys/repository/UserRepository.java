package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.Lessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessorRepository extends JpaRepository<Lessor, Integer> {
}
