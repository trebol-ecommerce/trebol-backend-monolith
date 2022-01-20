package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRolesJpaCrudServiceTest {

  @Mock IUserRolesJpaRepository userRolesRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserRolePojo, UserRole> userRolesConverterMock;

  @Test
  void sanity_check() {
    UserRolesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long roleId = 1L;
    String roleName = "test-role";
    UserRolePojo example = new UserRolePojo(roleName);
    UserRole persistedEntity = new UserRole(roleId, roleName);
    when(userRolesRepositoryMock.findByName(roleName)).thenReturn(Optional.of(persistedEntity));
    UserRolesJpaCrudServiceImpl service = instantiate();

    Optional<UserRole> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), roleId);
    assertEquals(match.get().getName(), roleName);
  }

  private UserRolesJpaCrudServiceImpl instantiate() {
    return new UserRolesJpaCrudServiceImpl(
        userRolesRepositoryMock,
        userRolesConverterMock
    );
  }

}
