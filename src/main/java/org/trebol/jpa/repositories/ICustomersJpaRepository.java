package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ICustomersJpaRepository
    extends IJpaRepository<Customer> {

  @Query(value = "SELECT c FROM Customer c JOIN FETCH c.person p WHERE p.idNumber = :idNumber")
  Optional<Customer> findByPersonIdNumber(@Param("idNumber") String idNumber);
}
