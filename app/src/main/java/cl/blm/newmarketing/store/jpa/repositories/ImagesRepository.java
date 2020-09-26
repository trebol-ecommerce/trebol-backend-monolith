package cl.blm.newmarketing.store.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.store.jpa.GenericRepository;
import cl.blm.newmarketing.store.jpa.entities.Image;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ImagesRepository
    extends GenericRepository<Image, Integer> {

}
