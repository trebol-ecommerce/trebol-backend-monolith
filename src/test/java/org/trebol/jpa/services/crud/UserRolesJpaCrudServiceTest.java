package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRolesJpaCrudServiceTest {
  @Mock IUserRolesJpaRepository userRolesRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserRolePojo, UserRole> userRolesConverterMock;
  private GenericCrudJpaService<UserRolePojo, UserRole> instance;

  @BeforeEach
  void beforeEach() {
    instance =  new UserRolesJpaCrudServiceImpl(
            userRolesRepositoryMock,
            userRolesConverterMock
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long roleId = 1L;
    String roleName = "test-role";
    UserRolePojo example = new UserRolePojo(roleName);
    UserRole persistedEntity = new UserRole(roleId, roleName);
    when(userRolesRepositoryMock.findByName(roleName)).thenReturn(Optional.of(persistedEntity));

    Optional<UserRole> match = instance.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), roleId);
    assertEquals(match.get().getName(), roleName);
  }
}
