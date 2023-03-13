package org.trebol.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.trebol.config.SecurityProperties;
import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRolePermission;
import org.trebol.jpa.repositories.UserRolePermissionsRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.security.services.UserPermissionsService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
  @InjectMocks UserDetailsServiceImpl instance;
  @Mock UsersRepository usersRepositoryMock;
  @Mock UserRolePermissionsRepository rolePermissionsRepositoryMock;
  @Mock UserPermissionsService userPermissionsServiceMock;
  @Mock SecurityProperties securityPropertiesMock;
  User dummyUser;
  Set<Permission> dummyPermissions;

  @BeforeEach
  void beforeEach() {
    dummyUser = User.builder()
      .name(ANY)
      .password(ANY)
      .build();
    dummyPermissions = Set.of(
      Permission.builder()
        .id(1L)
        .code(ANY)
        .build()
    );
  }

  @Test
  void loads_users_by_name() {
    assertFalse(securityPropertiesMock.isGuestUserEnabled());
    List<String> expectedAuthorities = dummyPermissions.stream()
      .map(Permission::getCode)
      .collect(Collectors.toList());
    when(usersRepositoryMock.findByNameWithRole(anyString())).thenReturn(Optional.of(dummyUser));
    when(userPermissionsServiceMock.loadPermissionsForUser(any(User.class))).thenReturn(dummyPermissions);

    UserDetailsPojo result = instance.loadUserByUsername(ANY);
    List<String> resultAuthorities = result.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());
    assertEquals(expectedAuthorities, resultAuthorities);
    assertEquals(dummyUser.getName(), result.getUsername());
    assertEquals(dummyUser.getPassword(), result.getPassword());
  }

  @Test
  void loads_guest_user() {
    String GUEST_USERNAME = "GUEST";
    List<UserRolePermission> rolePermissions = dummyPermissions.stream()
      .map(permission -> UserRolePermission.builder()
        .permission(permission)
        .build())
      .collect(Collectors.toList());
    List<String> expectedAuthorities = rolePermissions.stream()
      .map(userRolePermission -> userRolePermission.getPermission().getCode())
      .collect(Collectors.toList());
    when(securityPropertiesMock.isGuestUserEnabled()).thenReturn(true);
    when(securityPropertiesMock.getGuestUserName()).thenReturn(GUEST_USERNAME);
    when(rolePermissionsRepositoryMock.deepFindPermissionsByUserRoleId(anyLong())).thenReturn(rolePermissions);

    // will not accept just any username as to create a guest session
    assertThrows(UsernameNotFoundException.class, () -> instance.loadUserByUsername(ANY));
    verifyNoInteractions(rolePermissionsRepositoryMock);

    UserDetailsPojo result = instance.loadUserByUsername(GUEST_USERNAME);
    assertEquals(GUEST_USERNAME, result.getUsername());
    assertEquals(GUEST_USERNAME, result.getPassword());
    assertNotEquals(dummyUser.getName(), result.getUsername());
    assertNotEquals(dummyUser.getPassword(), result.getPassword());
    List<String> resultAuthorities = result.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList());
    assertEquals(expectedAuthorities, resultAuthorities);
  }
}
