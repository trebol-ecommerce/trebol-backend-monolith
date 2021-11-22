package org.trebol.jpa.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.IJpaRepository;
import org.trebol.jpa.entities.ProductCategory;

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

  Optional<Product> findByBarcode(String barcode);

  @Modifying
  @Transactional
  @Query("UPDATE Product p SET p.category = null WHERE p.category IN (:categories)")
  void orphanizeByCategories(@Param("categories") Collection<ProductCategory> categories);
}
