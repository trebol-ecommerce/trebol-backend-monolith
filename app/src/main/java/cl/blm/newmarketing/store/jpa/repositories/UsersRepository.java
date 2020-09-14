package cl.blm.newmarketing.store.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.name = :name")
  public Optional<User> findByNameWithRole(@Param("name") String name);
}
