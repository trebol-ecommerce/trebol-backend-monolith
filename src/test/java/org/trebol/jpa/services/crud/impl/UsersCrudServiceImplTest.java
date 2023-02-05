/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.crud.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.AccountProtectionViolationException;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.config.SecurityProperties;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.crud.impl.UsersCrudServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersCrudServiceImplTest {
  @InjectMocks UsersCrudServiceImpl instance;
  @Mock UsersRepository usersRepositoryMock;
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
