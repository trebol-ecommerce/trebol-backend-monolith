package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserRolesJpaCrudServiceTest {

  @Mock IUserRolesJpaRepository userRolesRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserRolePojo, UserRole> userRolesConverterMock;

  @Test
  public void sanity_check() {
    UserRolesJpaCrudServiceImpl service = new UserRolesJpaCrudServiceImpl(
        userRolesRepositoryMock,
        userRolesConverterMock
    );
    assertNotNull(service);
  }

}
