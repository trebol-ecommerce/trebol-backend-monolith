package cl.blm.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.blm.trebol.jpa.GenericRepository;
import cl.blm.trebol.jpa.entities.ProductImage;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductImagesRepository
    extends GenericRepository<ProductImage, Integer> {

  @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id")
  public Iterable<ProductImage> deepFindProductImagesByProductId(@Param("id") Integer id);
}
