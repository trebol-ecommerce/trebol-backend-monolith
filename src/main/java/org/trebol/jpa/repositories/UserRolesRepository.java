package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.UserRole;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UserRolesRepository
    extends GenericRepository<UserRole, Integer> {

  @Query
  Optional<UserRole> findByName(String name);

}
