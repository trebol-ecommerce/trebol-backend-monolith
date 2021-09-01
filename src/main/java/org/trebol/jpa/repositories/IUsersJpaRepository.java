package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.User;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IUsersJpaRepository
    extends IJpaRepository<User> {
  
  Optional<User> findByName(String name);

  @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.name = :name")
  Optional<User> findByNameWithRole(@Param("name") String name);

  @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.name = :name")
  Optional<User> findByNameWithProfile(@Param("name") String name);

  @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.id = :id")
  Optional<User> findByIdWithProfile(@Param("id") Long id);
}
