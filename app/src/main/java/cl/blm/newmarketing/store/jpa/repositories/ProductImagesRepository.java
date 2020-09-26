package cl.blm.newmarketing.store.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.store.jpa.GenericRepository;
import cl.blm.newmarketing.store.jpa.entities.ProductImage;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ProductImagesRepository
    extends GenericRepository<ProductImage, Integer> {

}
