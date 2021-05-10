package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.UserRolePermission;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UserRolePermissionsRepository
    extends GenericRepository<UserRolePermission, Integer> {

  @Query("SELECT urp FROM UserRolePermission urp JOIN FETCH urp.permission WHERE urp.userRole.id = :userRoleId")
  public Iterable<UserRolePermission> deepFindPermissionsByUserRoleId(@Param("userRoleId") Integer userRoleId);
}
