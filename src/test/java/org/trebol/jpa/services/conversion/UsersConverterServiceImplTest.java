package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UsersConverterServiceImplTest {
  @InjectMocks UsersConverterServiceImpl sut;
  @Mock UsersRepository userRepository;
  @Mock UserRolesRepository rolesRepository;
  @Mock PeopleConverterService peopleService;
  @Mock PeopleRepository peopleRepository;
  @Mock PasswordEncoder passwordEncoder;
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
  void converts_to_pojo() {
    when(peopleService.convertToPojo(any(Person.class))).thenReturn(PersonPojo.builder().build());

    UserPojo actual = sut.convertToPojo(user);

    assertNotNull(actual.getPerson());
    assertEquals(user.getId(), actual.getId());
    assertEquals(user.getName(), actual.getName());
  }

  @Test
  void does_not_accept_empty_roles_for_new_entities() {
    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(userPojo));
    assertEquals("The user was not given any role", badInputException.getMessage());
  }

  @Test
  void does_not_accept_unexisting_roles_for_new_entities() {
    userPojo.setRole(ANY);
    when(rolesRepository.findByName(anyString())).thenReturn(Optional.empty());

    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(userPojo));
    assertEquals("The specified user role does not exist", badInputException.getMessage());
  }

  @Test
  void converts_to_new_entity() throws BadInputException {
    final Person person = new Person();
    person.setIdNumber(ANY);
    final UserRole userRole = new UserRole();
    userRole.setName(ANY);
    final PersonPojo personPojo = PersonPojo.builder().idNumber(ANY).build();
    userPojo.setPassword(ANY);
    userPojo.setRole(ANY);
    userPojo.setPerson(personPojo);
    when(passwordEncoder.encode(anyString())).thenReturn(ANY);
    when(peopleRepository.findByIdNumber(anyString())).thenReturn(Optional.of(person));
    when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(userRole));

    User actual = sut.convertToNewEntity(userPojo);

    assertNotNull(actual.getPerson());
    assertEquals(userPojo.getPerson().getIdNumber(), actual.getPerson().getIdNumber());
    assertNotNull(actual.getUserRole());
    assertEquals(userPojo.getRole(), actual.getUserRole().getName());
  }
}
