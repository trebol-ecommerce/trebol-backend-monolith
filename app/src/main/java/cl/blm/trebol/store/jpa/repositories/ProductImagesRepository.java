package cl.blm.trebol.store.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.blm.trebol.store.jpa.GenericRepository;
import cl.blm.trebol.store.jpa.entities.ProductImage;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductImagesRepository
    extends GenericRepository<ProductImage, Integer> {

  @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id")
  public Iterable<ProductImage> deepFindProductImagesByProductId(@Param("id") Integer id);

    @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.image WHERE pi.product.id = :id ORDER BY pi.id")
  public Optional<ProductImage> deepFindFirstProductImageByProductId(@Param("id") Integer id);
}
