package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.pojo.UserRolePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRolesCrudServiceImplTest {
  @InjectMocks UserRolesCrudServiceImpl instance;
  @Mock UserRolesRepository userRolesRepositoryMock;

  @Test
  void finds_by_name() throws BadInputException {
    String roleName = "test-role";
    UserRolePojo input = UserRolePojo.builder()
      .name(roleName)
      .build();
    UserRole persistedEntity = new UserRole(1L, roleName);
    when(userRolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(persistedEntity));

    Optional<UserRole> match = instance.getExisting(input);

    verify(userRolesRepositoryMock).findByName(roleName);
    assertTrue(match.isPresent());
    assertEquals(persistedEntity, match.get());
  }
}
