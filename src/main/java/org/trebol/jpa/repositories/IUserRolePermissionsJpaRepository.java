package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.UserRolePermission;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IUserRolePermissionsJpaRepository
  extends IJpaRepository<UserRolePermission> {

  @Query("SELECT urp FROM UserRolePermission urp JOIN FETCH urp.permission WHERE urp.userRole.id = :userRoleId")
  Iterable<UserRolePermission> deepFindPermissionsByUserRoleId(@Param("userRoleId") Long userRoleId);
}
