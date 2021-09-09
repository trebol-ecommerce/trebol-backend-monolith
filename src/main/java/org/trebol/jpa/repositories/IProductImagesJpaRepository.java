package org.trebol.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IProductImagesJpaRepository
  extends IJpaRepository<ProductImage> {

  List<ProductImage> findByProductId(long productId);

  @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id")
  List<ProductImage> deepFindProductImagesByProductId(@Param("id") long id);

  @Modifying
  @Query("DELETE FROM ProductImage pi WHERE pi.product.id = :id")
  int deleteByProductId(@Param("id") long id);
}
