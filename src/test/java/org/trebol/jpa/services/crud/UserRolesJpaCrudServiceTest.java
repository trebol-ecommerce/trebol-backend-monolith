package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRolesJpaCrudServiceTest {

  @Mock IUserRolesJpaRepository userRolesRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserRolePojo, UserRole> userRolesConverterMock;

  @Test
  public void sanity_check() {
    UserRolesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_name() throws BadInputException {
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
