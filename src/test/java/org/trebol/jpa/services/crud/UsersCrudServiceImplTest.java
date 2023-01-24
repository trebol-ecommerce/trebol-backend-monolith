package org.trebol.jpa.services.crud;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
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
import org.trebol.jpa.repositories.UsersJpaRepository;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersCrudServiceImplTest {
  @InjectMocks UsersCrudServiceImpl instance;
  @Mock UsersJpaRepository usersRepositoryMock;
  @Mock SecurityProperties securityPropertiesMock;

  @Test
  void finds_by_name() throws BadInputException {
    String userName = "test-user";
    UserPojo input = UserPojo.builder()
      .name(userName)
      .build();
    User expectedResult = new User(1L,
      userName,
      "test-password",
      new Person("111111111"),
      new UserRole(1000L, "test-user"));
    when(usersRepositoryMock.findByName(userName)).thenReturn(Optional.of(expectedResult));

    Optional<User> match = instance.getExisting(input);

    verify(usersRepositoryMock).findByName(userName);
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void delete_ProtectedAccount_ThrowsBadInputException() {
    Long userId = 1L;
    User userMock = new User(userId,
      "test-user",
      "test-password",
      new Person("111111111"),
      new UserRole(1000L, "test-role"));

    when(securityPropertiesMock.isAccountProtectionEnabled()).thenReturn(true);
    when(securityPropertiesMock.getProtectedAccountId()).thenReturn(userId);
    when(usersRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(userMock));

    assertThrows(AccountProtectionViolationException.class, () -> instance.delete(new BooleanBuilder()));
  }
}
