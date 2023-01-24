package org.trebol.operation.services;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.conversion.IPeopleConverterService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.RegistrationPojo;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  @InjectMocks RegistrationServiceImpl instance;
  @Mock IUsersJpaRepository usersRepositoryMock;
  @Mock IPeopleConverterService peopleConverterService;
  @Mock IPeopleJpaRepository peopleRepositoryMock;
  @Mock IUserRolesJpaRepository rolesRepositoryMock;
  @Mock PasswordEncoder passwordEncoderMock;
  @Mock ICustomersJpaRepository customerRepositoryMock;
  PersonPojo personPojoMock;
  RegistrationPojo regPojoMock;
  Person personMock;
  Optional<UserRole> customerRoleMock;

  @BeforeEach
  void beforeEach() {
    // Default mock objects
    personPojoMock = PersonPojo.builder()
      .id(1L)
      .firstName("firstName")
      .lastName("lastName")
      .idNumber("1")
      .email("email@example.com")
      .phone1("+123 456")
      .phone2("+123 456")
      .build();
    regPojoMock = RegistrationPojo.builder()
      .name("name")
      .password("password")
      .profile(personPojoMock)
      .build();
    personMock = new Person(1L, "firstName", "lastName", "1", "email@example.com", "+123 456", "+123 456");
    customerRoleMock = Optional.of(new UserRole(1L, "Customer"));

    // Reset mocks
    reset(usersRepositoryMock);
    reset(peopleRepositoryMock);
    reset(rolesRepositoryMock);
  }

  @DisplayName("User and Id doesn't already exists, should pass")
  @Test
  void EverythingCorrect_NoException() throws BadInputException {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleConverterService.convertToNewEntity(any(PersonPojo.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
    // inside convertToUser method
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(customerRoleMock);

    assertDoesNotThrow(() -> instance.register(regPojoMock));
  }

  @DisplayName("Save and flush must be called on repositories")
  @Test
  void SaveCalledOnRepository() throws EntityExistsException, BadInputException {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleConverterService.convertToNewEntity(any(PersonPojo.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
    // inside convertToUser method
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(customerRoleMock);

    instance.register(regPojoMock);

    verify(peopleRepositoryMock, times(1)).saveAndFlush(any(Person.class));
    verify(usersRepositoryMock, times(1)).saveAndFlush(any(User.class));
    verify(customerRepositoryMock, times(1)).saveAndFlush(any(Customer.class));
  }

  @DisplayName("User with same name already exists, EntityExistsException")
  @Test
  void NameAlreadyExists_EntityExistsException() {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(true); // Name already exists

    assertThrows(EntityExistsException.class, () -> instance.register(regPojoMock));
  }

  @DisplayName("Person with same ID already exists, EntityExistsException")
  @Test
  void IdAlreadyExists_EntityExistsException() throws BadInputException {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleConverterService.convertToNewEntity(any(PersonPojo.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(true); // ID already exists

    assertThrows(EntityExistsException.class, () -> instance.register(regPojoMock));
  }

  @DisplayName("Customer Role not found, IllegalStateException")
  @Test
  void CustomerRoleNotFound_IllegalStateException() throws BadInputException {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleConverterService.convertToNewEntity(any(PersonPojo.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
    // inside convertToUser method
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> instance.register(regPojoMock));
  }

}
