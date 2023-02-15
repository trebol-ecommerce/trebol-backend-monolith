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

package org.trebol.jpa.services.patch.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class UsersPatchServiceImplTest {
  @InjectMocks UsersPatchServiceImpl instance;
  @Mock UserRolesRepository rolesRepositoryMock;
  @Mock PeopleRepository peopleRepositoryMock;
  @Mock PasswordEncoder passwordEncoderMock;
  User user;
  UserPojo userPojo;

  @BeforeEach
  void beforeEach() {
    user = new User();
    user.setName(ANY);
    user.setId(1L);
    final UserRole userRole = new UserRole();
    userRole.setName(ANY);
    user.setUserRole(userRole);
    Person person = new Person();
    user.setPerson(person);
    userPojo = UserPojo.builder()
      .id(1L)
      .name(ANY)
      .build();
  }

  @AfterEach
  void afterEach() {
    user = null;
    userPojo = null;
  }

  @Test
  void patches_entity_data() throws BadInputException {
    userPojo.setId(1L);
    userPojo.setName(ANY);
    userPojo.setRole(ANY);
    userPojo.setPassword(ANY);
    final PersonPojo personPojo = PersonPojo.builder().idNumber(ANY).build();
    userPojo.setPerson(personPojo);
    user.setId(1L);
    user.setName(ANY + " ");
    final UserRole role = new UserRole();
    role.setName(ANY + " ");
    user.setUserRole(role);
    user.setPassword(ANY + " ");
    final Person person = new Person();
    person.setIdNumber(ANY + " ");
    user.setPerson(person);
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(role));
    when(passwordEncoderMock.encode(anyString())).thenReturn(ANY);
    when(peopleRepositoryMock.findByIdNumber(anyString())).thenReturn(Optional.of(person));
    User actual = instance.patchExistingEntity(userPojo, user);
    assertEquals(ANY + " ", actual.getPerson().getIdNumber());
  }
}
