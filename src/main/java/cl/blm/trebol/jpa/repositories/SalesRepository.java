package cl.blm.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.jpa.GenericRepository;
import cl.blm.trebol.jpa.entities.Sell;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface SalesRepository
    extends GenericRepository<Sell, Integer> {

  @Query(value = "SELECT s FROM Sell s JOIN FETCH s.type JOIN FETCH s.customer JOIN FETCH s.salesperson", countQuery = "SELECT COUNT(s.id) FROM Sell s")
  Page<Sell> deepFindAll(Pageable pageable);

  @Query(value = "SELECT s FROM Sell s JOIN FETCH s.type JOIN FETCH s.customer JOIN FETCH s.salesperson", countQuery = "SELECT COUNT(s.id) FROM Sell s")
  Page<Sell> deepFindAll(Predicate filters, Pageable pageable);

  @Query(value = "SELECT s FROM Sell s JOIN FETCH s.type JOIN FETCH s.status JOIN FETCH s.customer JOIN FETCH s.salesperson"
      + " JOIN FETCH s.details WHERE s.id = :id")
  Optional<Sell> deepFindById(@Param("id") Integer id);
}
