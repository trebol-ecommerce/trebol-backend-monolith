package cl.blm.trebol.store.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.trebol.store.jpa.GenericRepository;
import cl.blm.trebol.store.jpa.entities.Session;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface SessionsRepository
    extends GenericRepository<Session, Long> {

}
