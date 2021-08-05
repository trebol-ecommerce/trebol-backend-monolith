package org.trebol.security.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.entities.UserRolePermission;
import org.trebol.jpa.repositories.UserRolePermissionsRepository;
import org.trebol.security.services.UserPermissionsService;

/**
 * Service required by the DaoAuthenticationProvider bean.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 */
@Service
public class UserPermissionsServiceImpl
    implements UserPermissionsService {

  private final UserRolePermissionsRepository userRolePermissionsRepository;

  @Autowired
  public UserPermissionsServiceImpl(UserRolePermissionsRepository userRolePermissionsRepository) {
    this.userRolePermissionsRepository = userRolePermissionsRepository;
  }

  @Override
  public Set<Permission> loadPermissionsForUser(User source) {
    UserRole sourceUserRole = source.getUserRole();
    Long userRoleId = sourceUserRole.getId();
    Iterable<UserRolePermission> userRolePermissions = userRolePermissionsRepository
        .deepFindPermissionsByUserRoleId(userRoleId);

    Set<Permission> targetList = new HashSet<>();
    for (UserRolePermission rolePermission : userRolePermissions) {
      Permission p = rolePermission.getPermission();
      targetList.add(p);
    }

    return targetList;
  }

}
