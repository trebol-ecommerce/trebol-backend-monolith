package cl.blm.newmarketing.store.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.store.jpa.GenericRepository;
import cl.blm.newmarketing.store.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UsersRepository
    extends GenericRepository<User, Integer> {

  public Optional<User> findByName(String name);
}
