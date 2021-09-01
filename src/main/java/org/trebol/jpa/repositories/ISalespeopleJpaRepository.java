package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ISalespeopleJpaRepository
    extends IJpaRepository<Salesperson> {

  @Query(value = "SELECT s FROM Salesperson s JOIN FETCH s.person p WHERE p.idNumber = :idNumber")
  Optional<Salesperson> findByPersonIdNumber(@Param("idNumber") String idNumber);

}
