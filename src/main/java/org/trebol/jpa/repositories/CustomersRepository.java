package org.trebol.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.Customer;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface CustomersRepository
    extends GenericRepository<Customer> {

  @Query(value = "SELECT c FROM Customer c JOIN FETCH c.person", countQuery = "SELECT c FROM Customer c")
  Page<Customer> deepReadAll(Pageable pageable);

  @Query(value = "SELECT c FROM Customer c JOIN FETCH c.person", countQuery = "SELECT c FROM Customer c")
  Page<Customer> deepReadAll(Predicate filters, Pageable pageable);
}
