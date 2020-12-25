package cl.blm.trebol.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.jpa.GenericRepository;
import cl.blm.trebol.jpa.entities.Salesperson;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface SalespeopleRepository
    extends GenericRepository<Salesperson, Integer> {

  @Query(value = "SELECT s FROM Salesperson s JOIN FETCH s.person", countQuery = "SELECT s FROM Salesperson s")
  Page<Salesperson> deepReadAll(Pageable pageable);

  @Query(value = "SELECT s FROM Salesperson s JOIN FETCH s.person", countQuery = "SELECT s FROM Salesperson s")
  Page<Salesperson> deepReadAll(Predicate filters, Pageable pageable);

}
