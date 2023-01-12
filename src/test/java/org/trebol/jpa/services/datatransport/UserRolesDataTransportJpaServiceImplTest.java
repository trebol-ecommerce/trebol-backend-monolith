package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.pojo.UserRolePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserRolesDataTransportJpaServiceImplTest {
  @InjectMocks UserRolesDataTransportJpaServiceImpl sut;
  UserRole userRole;
  UserRolePojo userRolePojo;

  @BeforeEach
  void beforeEach() {
    userRole = new UserRole();
    userRole.setName("ANY");
    userRole.setId(1L);
    userRolePojo = UserRolePojo.builder()
      .id(1L)
      .name("ANY")
      .build();
  }

  @AfterEach
  void afterEach() {
    userRole = null;
    userRolePojo = null;
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    UserRole actual = sut.applyChangesToExistingEntity(userRolePojo, userRole);
    assertEquals(1L, actual.getId());
  }
}
