package org.trebol.jpa.services.datatransport;

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
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UsersDataTransportJpaServiceImplTest {
  @InjectMocks UsersDataTransportJpaServiceImpl sut;
  @Mock IUserRolesJpaRepository rolesRepository;
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
  void testApplyChangesToExistingEntity() throws BadInputException {
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
    when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(role));
    when(passwordEncoder.encode(anyString())).thenReturn(ANY);
    when(peopleRepository.findByIdNumber(anyString())).thenReturn(Optional.of(person));

    User actual = sut.applyChangesToExistingEntity(userPojo, user);

    assertEquals(ANY + " ", actual.getPerson().getIdNumber());
  }
}
