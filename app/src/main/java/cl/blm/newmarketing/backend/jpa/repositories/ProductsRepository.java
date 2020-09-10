package cl.blm.newmarketing.backend.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.jpa.GenericRepository;
import cl.blm.newmarketing.backend.jpa.entities.Product;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductsRepository
    extends GenericRepository<Product, Integer> {

  @Query(value = "SELECT p FROM Product p JOIN FETCH p.productType", countQuery = "SELECT p FROM Product p")
  Page<Product> deepReadAll(Pageable pageable);

  @Query(value = "SELECT p FROM Product p JOIN FETCH p.productType", countQuery = "SELECT p FROM Product p")
  Page<Product> deepReadAll(Predicate filters, Pageable pageable);

}
