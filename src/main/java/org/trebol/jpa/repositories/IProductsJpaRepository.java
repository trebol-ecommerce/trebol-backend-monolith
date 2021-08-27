package org.trebol.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.Product;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IProductsJpaRepository
    extends IJpaRepository<Product> {

  @Query(value = "SELECT p FROM Product p JOIN FETCH p.productCategory", countQuery = "SELECT p FROM Product p")
  Page<Product> deepReadAll(Pageable pageable);

  @Query(value = "SELECT p FROM Product p JOIN FETCH p.productCategory", countQuery = "SELECT p FROM Product p")
  Page<Product> deepReadAll(Predicate filters, Pageable pageable);

}
