package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersJpaCrudServiceTest {
  @Mock IUsersJpaRepository usersRepositoryMock;
  @Mock ITwoWayConverterJpaService<UserPojo, User> usersConverterMock;
  private GenericCrudJpaService<UserPojo, User> instance;

  @BeforeEach
  void setUp() {
    instance = new UsersJpaCrudServiceImpl(
            usersRepositoryMock,
            usersConverterMock
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long userId = 1L;
    String userName = "test-user";
    String userPassword = "test-password";
    String idNumber = "111111111";
    Person person = new Person(idNumber);
    Long roleId = 2L;
    String roleName = "test-role";
    UserRole role = new UserRole(roleId, roleName);
    UserPojo example = new UserPojo(userName);
    User persistedEntity = new User(userId, userName, userPassword, person, role);
    when(usersRepositoryMock.findByName(userName)).thenReturn(Optional.of(persistedEntity));

    Optional<User> match = instance.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), userId);
    assertEquals(match.get().getName(), userName);
    assertEquals(match.get().getPassword(), userPassword);
    assertEquals(match.get().getPerson().getIdNumber(), idNumber);
    assertEquals(match.get().getUserRole().getName(), roleName);
  }

}
