package org.trebol.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ISalespeopleJpaRepository
    extends IJpaRepository<Salesperson> {

  @Query(value = "SELECT s FROM Salesperson s JOIN FETCH s.person", countQuery = "SELECT s FROM Salesperson s")
  Page<Salesperson> deepReadAll(Pageable pageable);

  @Query(value = "SELECT s FROM Salesperson s JOIN FETCH s.person", countQuery = "SELECT s FROM Salesperson s")
  Page<Salesperson> deepReadAll(Predicate filters, Pageable pageable);

}
