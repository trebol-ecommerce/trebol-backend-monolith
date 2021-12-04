package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UsersJpaCrudServiceTest {

  @Mock IUsersJpaRepository usersRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserPojo, User> usersConverterMock;

  @Test
  public void sanity_check() {
    UsersJpaCrudServiceImpl service = new UsersJpaCrudServiceImpl(
        usersRepositoryMock,
        usersConverterMock
    );
    assertNotNull(service);
  }

}
