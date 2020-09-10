package cl.blm.newmarketing.backend.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.jpa.GenericRepository;
import cl.blm.newmarketing.backend.jpa.entities.ProductType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductTypesRepository
    extends GenericRepository<ProductType, Integer> {

  @Query(value = "SELECT t FROM ProductType t JOIN FETCH t.productFamily", countQuery = "SELECT t FROM ProductType t")
  Page<ProductType> deepReadAll(Pageable pageable);

  @Query(value = "SELECT t FROM ProductType t JOIN FETCH t.productFamily", countQuery = "SELECT t FROM ProductType t")
  Page<ProductType> deepReadAll(Predicate predicate, Pageable pageable);
}
