package cl.blm.newmarketing.backend.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.backend.jpa.GenericRepository;
import cl.blm.newmarketing.backend.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UsersRepository
    extends GenericRepository<User, Integer> {

  public Optional<User> findByName(String name);
}
