package org.trebol.jpa.services.crud;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.config.SecurityProperties;
import org.trebol.exceptions.AccountProtectionViolationException;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersJpaCrudServiceImplTest {
  @InjectMocks UsersJpaCrudServiceImpl instance;
  @Mock IUsersJpaRepository usersRepositoryMock;
  @Mock SecurityProperties securityPropertiesMock;

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
    User persistedEntity = new User(userId, userName, userPassword, person, role);
    when(usersRepositoryMock.findByName(userName)).thenReturn(Optional.of(persistedEntity));

    Optional<User> match = instance.getExisting(UserPojo.builder().name(userName).build());

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), userId);
    assertEquals(match.get().getName(), userName);
    assertEquals(match.get().getPassword(), userPassword);
    assertEquals(match.get().getPerson().getIdNumber(), idNumber);
    assertEquals(match.get().getUserRole().getName(), roleName);
  }
  
  @Test
  void delete_ProtectedAccount_ThrowsBadInputException() {
    Long userId = 1L;
    String userName = "test-user";
    String userPassword = "test-password";
    String idNumber = "111111111";
    Person person = new Person(idNumber);
    Long roleId = 2L;
    String roleName = "test-role";
    UserRole role = new UserRole(roleId, roleName);
    User userMock = new User(userId, userName, userPassword, person, role);

    Predicate predicateMock = new Predicate() {
      @Override
      public Class<? extends Boolean> getType() {
        return null;
      }

      @Override
      public <R, C> R accept(Visitor<R, C> v, C context) {
        return null;
      }

      @Override
      public Predicate not() {
        return null;
      }
    };

    when(securityPropertiesMock.isAccountProtectionEnabled()).thenReturn(true);
    when(securityPropertiesMock.getProtectedAccountId()).thenReturn(userId);
    when(usersRepositoryMock.findOne(predicateMock)).thenReturn(Optional.of(userMock));

    assertThrows(AccountProtectionViolationException.class, () -> instance.delete(predicateMock));
  }

}
