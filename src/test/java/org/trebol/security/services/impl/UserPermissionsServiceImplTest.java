package org.trebol.security.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.entities.UserRolePermission;
import org.trebol.jpa.repositories.UserRolePermissionsRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UserPermissionsServiceImplTest {
  @InjectMocks UserPermissionsServiceImpl instance;
  @Mock UserRolePermissionsRepository userRolePermissionsRepositoryMock;

  @Test
  void loads_permissions_for_user_roles() {
    User input = User.builder()
      .userRole(UserRole.builder()
        .id(1L)
        .build())
      .build();
    List<UserRolePermission> rolePermissions = List.of(
      UserRolePermission.builder()
        .permission(Permission.builder()
          .description(ANY)
          .build())
        .build()
    );
    when(userRolePermissionsRepositoryMock.deepFindPermissionsByUserRoleId(anyLong())).thenReturn(rolePermissions);

    Set<Permission> result = instance.loadPermissionsForUser(input);
    assertFalse(result.isEmpty());
    Set<Permission> expectedResult = rolePermissions.stream()
      .map(UserRolePermission::getPermission)
      .collect(Collectors.toSet());
    assertEquals(expectedResult, result);
  }
}
