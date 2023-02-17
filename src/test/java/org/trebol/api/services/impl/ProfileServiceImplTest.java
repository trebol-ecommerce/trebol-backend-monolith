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

package org.trebol.api.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.exceptions.PersonNotFoundException;
import org.trebol.jpa.exceptions.UserNotFoundException;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.conversion.PeopleConverterService;
import org.trebol.jpa.services.crud.PeopleCrudService;
import org.trebol.jpa.services.patch.PeoplePatchService;
import org.trebol.testing.PeopleTestHelper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
  @InjectMocks ProfileServiceImpl instance;
  @Mock UsersRepository usersRepositoryMock;
  @Mock PeopleCrudService peopleServiceMock;
  @Mock PeopleConverterService peopleConverterMock;
  @Mock PeoplePatchService peoplePatchService;
  @Mock PeopleRepository peopleRepositoryMock;
  PeopleTestHelper peopleTestHelper = new PeopleTestHelper();
  User existingUser;

  @BeforeEach
  void beforeEach() {
    existingUser = User.builder() // without profile
      .id(1L)
      .name("userName")
      .password("password")
      .userRole(UserRole.builder()
        .id(1L)
        .name("roleName")
        .build())
      .build();
  }

  @Test
  void creates_new_profile_data_when_user_does_not_have_one() throws BadInputException {
    Person newProfile = peopleTestHelper.personEntityAfterCreation();
    User existingUserBefore = new User(existingUser);
    User existingUserAfter = new User(existingUser);
    existingUserAfter.setPerson(newProfile);
    String name = existingUser.getName();
    PersonPojo input = peopleTestHelper.personPojoBeforeCreation();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUserBefore));
    when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.empty());
    when(peopleConverterMock.convertToNewEntity(any(PersonPojo.class))).thenReturn(newProfile);
    when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(newProfile);
    when(usersRepositoryMock.saveAndFlush(any(User.class))).thenReturn(existingUserAfter); // binds Person entity to User entity
    assertDoesNotThrow(() -> instance.updateProfileForUserWithName(name, input));
    verify(peopleRepositoryMock, times(1)).saveAndFlush(newProfile);
    verify(usersRepositoryMock, times(1)).saveAndFlush(existingUserBefore);
  }

  @Test
  void updates_existing_profile_data() throws BadInputException {
    Person existingProfile = peopleTestHelper.personEntityAfterCreation();
    existingUser.setPerson(existingProfile);
    String name = existingUser.getName();
    PersonPojo input = peopleTestHelper.personPojoBeforeCreation();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUser));
    when(peoplePatchService.patchExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(existingProfile);
    assertDoesNotThrow(() -> instance.updateProfileForUserWithName(name, input));
    verify(peopleRepositoryMock, times(1)).saveAndFlush(any(Person.class));
  }

  @Test
  void fetches_profile_data() {
    Person existingProfile = peopleTestHelper.personEntityAfterCreation();
    existingUser.setPerson(existingProfile);
    String name = existingUser.getName();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUser));
    assertDoesNotThrow(() -> instance.getProfileFromUserName(name));
  }

  @Test
  void either_method_may_throw_UserNotFoundException() {
    String name = existingUser.getName();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    List.of(
      assertThrows(UserNotFoundException.class, () -> instance.getProfileFromUserName(name)),
      assertThrows(UserNotFoundException.class, () -> instance.updateProfileForUserWithName(name, null))
    ).forEach(result -> assertEquals("There is no account with the specified username", result.getMessage()));
  }

  @Test
  void fetching_may_throw_PersonNotFoundException() {
    String name = existingUser.getName();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUser));
    PersonNotFoundException result = assertThrows(PersonNotFoundException.class, () -> instance.getProfileFromUserName(name));
    assertEquals("The account does not have an associated profile", result.getMessage());
  }

  @Test
  void binds_existing_profile_data() throws BadInputException {
    Person existingProfile = peopleTestHelper.personEntityAfterCreation();
    String name = existingUser.getName();
    PersonPojo input = peopleTestHelper.personPojoBeforeCreation();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUser));
    when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.of(existingProfile));
    when(usersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.empty());
    when(usersRepositoryMock.saveAndFlush(any(User.class))).thenReturn(existingUser);
    assertDoesNotThrow(() -> instance.updateProfileForUserWithName(name, input));
    verify(usersRepositoryMock, times(1)).saveAndFlush(any(User.class));
  }

  @Test
  void cannot_bind_existing_profile_data_if_it_is_already_bound_to_another_user() throws BadInputException {
    Person existingProfile = peopleTestHelper.personEntityAfterCreation();
    String name = existingUser.getName();
    PersonPojo input = peopleTestHelper.personPojoBeforeCreation();
    when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingUser));
    when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.of(existingProfile));
    when(usersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(existingUser));
    BadInputException result = assertThrows(BadInputException.class, () -> instance.updateProfileForUserWithName(name, input));
    assertEquals("Person profile is associated to another account. Cannot use it.", result.getMessage());
  }

}
