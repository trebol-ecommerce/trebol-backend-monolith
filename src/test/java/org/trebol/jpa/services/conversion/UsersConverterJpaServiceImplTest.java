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
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UsersConverterJpaServiceImplTest {
  @InjectMocks UsersConverterJpaServiceImpl sut;
  @Mock IUsersJpaRepository userRepository;
  @Mock IUserRolesJpaRepository rolesRepository;
  @Mock IPeopleConverterJpaService peopleService;
  @Mock IPeopleJpaRepository peopleRepository;
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
  void testConvertToPojo() {
    when(peopleService.convertToPojo(any(Person.class))).thenReturn(PersonPojo.builder().build());

    UserPojo actual = sut.convertToPojo(user);

    assertNotNull(actual.getPerson());
    assertEquals(user.getId(), actual.getId());
    assertEquals(user.getName(), actual.getName());
  }

  @Test
  void testConvertToNewEntityBadInputException() throws BadInputException {
    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(userPojo));
    assertEquals("The user does not have a role", badInputException.getMessage());

    userPojo.setRole(ANY);
    when(rolesRepository.findByName(anyString())).thenReturn(Optional.empty());

    BadInputException badInputException2 = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(userPojo));
    assertEquals("The specified user role does not exist", badInputException2.getMessage());
  }

  @Test
  void testConvertToNewEntity() throws BadInputException {
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
