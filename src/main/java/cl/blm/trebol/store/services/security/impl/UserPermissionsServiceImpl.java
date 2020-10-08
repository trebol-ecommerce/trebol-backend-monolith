package cl.blm.trebol.store.services.security.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.blm.trebol.store.jpa.entities.Permission;
import cl.blm.trebol.store.jpa.entities.User;
import cl.blm.trebol.store.jpa.entities.UserRole;
import cl.blm.trebol.store.jpa.entities.UserRolePermission;
import cl.blm.trebol.store.jpa.repositories.UserRolePermissionsRepository;
import cl.blm.trebol.store.services.security.UserPermissionsService;

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
    Integer userRoleId = sourceUserRole.getId();
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
