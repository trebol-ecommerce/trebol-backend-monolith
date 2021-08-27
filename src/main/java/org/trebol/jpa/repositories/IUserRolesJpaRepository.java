package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IUserRolesJpaRepository
    extends IJpaRepository<UserRole> {

  @Query
  Optional<UserRole> findByName(String name);

}
