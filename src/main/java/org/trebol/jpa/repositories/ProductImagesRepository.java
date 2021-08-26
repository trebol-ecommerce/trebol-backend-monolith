package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.ProductImage;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductImagesRepository
  extends GenericRepository<ProductImage> {

  @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id")
  public Iterable<ProductImage> deepFindProductImagesByProductId(@Param("id") Long id);
}
