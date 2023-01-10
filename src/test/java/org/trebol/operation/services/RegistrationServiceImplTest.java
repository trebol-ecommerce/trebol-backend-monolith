package org.trebol.operation.services;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
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
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.RegistrationPojo;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  @InjectMocks RegistrationServiceImpl instance;
  @Mock IUsersJpaRepository usersRepositoryMock;
  @Mock ConversionService conversionServiceMock;
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
    regPojoMock = mockRegPojo("name", "password", personPojoMock);
    personMock = mockPerson(1L, "firstName", "lastName", "1", "email@example.com", "+123 456", "+123 456");
    customerRoleMock = Optional.of(new UserRole(1L, "Customer"));

    // Reset mocks
    reset(usersRepositoryMock);
    reset(conversionServiceMock);
    reset(peopleRepositoryMock);
    reset(rolesRepositoryMock);
  }

  @DisplayName("User and Id doesn't already exists, should pass")
  @Test
  void EverythingCorrect_NoException() {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(conversionServiceMock.convert(any(PersonPojo.class), eq(Person.class))).thenReturn(personMock);
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
    when(conversionServiceMock.convert(any(PersonPojo.class), eq(Person.class))).thenReturn(personMock);
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
  void IdAlreadyExists_EntityExistsException() {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(conversionServiceMock.convert(any(PersonPojo.class), eq(Person.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(true); // ID already exists

    assertThrows(EntityExistsException.class, () -> instance.register(regPojoMock));
  }

  @DisplayName("Customer Role not found, IllegalStateException")
  @Test
  void CustomerRoleNotFound_IllegalStateException() {
    when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(conversionServiceMock.convert(any(PersonPojo.class), eq(Person.class))).thenReturn(personMock);
    when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(false);
    when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
    // inside convertToUser method
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> instance.register(regPojoMock));
  }

  // Helper methods ---

  RegistrationPojo mockRegPojo(String name, String password, PersonPojo personPojo) {
    RegistrationPojo regPojoMock = new RegistrationPojo();
    regPojoMock.setName(name);
    regPojoMock.setPassword(password);
    regPojoMock.setProfile(personPojo);
    return regPojoMock;
  }

  Person mockPerson(Long id, String firstName, String lastName, String idNumber, String email,
                    String phone1, String phone2) {
    return new Person(id, firstName, lastName, idNumber, email, phone1, phone2);
  }

}
