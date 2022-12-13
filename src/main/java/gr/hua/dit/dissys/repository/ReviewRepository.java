package gr.hua.dit.dissys.repository;

import gr.hua.dit.dissys.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="reviews")
public interface ReviewRepository extends JpaRepository<Contract, Integer> {
}
