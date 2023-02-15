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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.services.conversion.PeopleConverterService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class UsersConverterServiceImplTest {
  @InjectMocks UsersConverterServiceImpl instance;
  @Mock UserRolesRepository rolesRepositoryMock;
  @Mock PeopleConverterService peopleServiceMock;
  @Mock PeopleRepository peopleRepositoryMock;
  @Mock PasswordEncoder passwordEncoderMock;

  @Test
  void converts_to_pojo() {
    User input = User.builder()
      .id(1L)
      .name(ANY)
      .person(Person.builder().build())
      .userRole(UserRole.builder()
        .name(ANY)
        .build())
      .build();
    PersonPojo expectedPersonPojo = PersonPojo.builder()
      .idNumber(ANY)
      .build();
    when(peopleServiceMock.convertToPojo(any(Person.class))).thenReturn(expectedPersonPojo);
    UserPojo actual = instance.convertToPojo(input);
    assertNotNull(actual);
    assertEquals(input.getId(), actual.getId());
    assertEquals(input.getName(), actual.getName());
    assertEquals(expectedPersonPojo, actual.getPerson());
  }

  @Test
  void does_not_accept_empty_roles_for_new_entities() {
    UserPojo userPojo = UserPojo.builder()
      .id(1L)
      .name(ANY)
      .build();
    BadInputException badInputException = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(userPojo));
    assertEquals("The user was not given any role", badInputException.getMessage());
  }

  @Test
  void does_not_accept_unexisting_user_roles_for_new_entities() {
    UserPojo userPojo = UserPojo.builder()
      .id(1L)
      .name(ANY)
      .role(ANY)
      .build();
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    BadInputException result = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(userPojo));
    assertEquals("The specified user role does not exist", result.getMessage());
  }

  @Test
  void converts_to_new_entity() throws BadInputException {
    UserPojo input = UserPojo.builder()
      .id(1L)
      .name(ANY)
      .password(ANY)
      .role(ANY)
      .person(PersonPojo.builder()
        .idNumber(ANY)
        .build())
      .build();
    Person expectedPerson = Person.builder().build();
    UserRole expectedUserRole = UserRole.builder().build();
    when(passwordEncoderMock.encode(anyString())).thenReturn(ANY);
    when(peopleRepositoryMock.findByIdNumber(anyString())).thenReturn(Optional.of(expectedPerson));
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedUserRole));
    User result = instance.convertToNewEntity(input);
    assertNotNull(result);
    assertEquals(expectedPerson, result.getPerson());
    assertEquals(expectedUserRole, result.getUserRole());
  }

  @Test
  void converts_to_new_entity_without_profile() throws BadInputException {
    UserPojo input = UserPojo.builder()
      .id(1L)
      .name(ANY)
      .password(ANY)
      .role(ANY)
      .build();
    when(passwordEncoderMock.encode(anyString())).thenReturn(ANY);
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(UserRole.builder().build()));
    User result = instance.convertToNewEntity(input);
    assertNotNull(result);
    assertNull(result.getPerson());
  }
}
