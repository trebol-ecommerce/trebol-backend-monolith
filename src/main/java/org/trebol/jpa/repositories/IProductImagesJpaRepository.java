package org.trebol.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.IJpaRepository;
import org.trebol.jpa.entities.Product;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IProductImagesJpaRepository
  extends IJpaRepository<ProductImage> {

  List<ProductImage> findByProductId(Long productId);

  @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id")
  List<ProductImage> deepFindProductImagesByProductId(@Param("id") Long id);
}
