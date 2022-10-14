package org.trebol.operation.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.RegistrationPojo;

import com.querydsl.core.types.Predicate;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
	
	@InjectMocks	RegistrationServiceImpl instance;	
	
	@Mock	IUsersJpaRepository usersRepositoryMock;
	
	@Mock	ConversionService conversionServiceMock;
	
	@Mock	IPeopleJpaRepository peopleRepositoryMock;
	
	@Mock	ICustomersJpaRepository customersRepositoryMock;
	
	@Mock	PasswordEncoder passwordEncoderMock;
	
	@Mock 	IUserRolesJpaRepository rolesRepositoryMock;
	
	@Mock	Logger loggerMock;
	
	PersonPojo personPojoMock;
	RegistrationPojo regPojoMock;
	Person personMock;
	
	@BeforeEach
	void beforeEach() {
		// PersonPojoMock
		Long id = 1L;
		String firstName = "firstName";
		String lastName  = "lastName";
		String idNumber = "1";
		String email = "email@example.com";
		String phone1 = "+123 456";
		String phone2 = "+123 456";
		personPojoMock = new PersonPojo(id, firstName, lastName, idNumber, email, phone1, phone2);
		
		// RegPojoMock
		String username = "name";
		String password = "password";
		regPojoMock = new RegistrationPojo();
		regPojoMock.setName(username);
		regPojoMock.setPassword(password);
		regPojoMock.setProfile(personPojoMock);
		
		// PersonMock - DONT TEST - assume the conversionService.convert() works correctly
		personMock = new Person(id, firstName, lastName, idNumber, email, phone1, phone2);		
		
		// CustomerRole
		Optional<UserRole> customerRole = Optional.of(new UserRole(1L, "Customer"));
		
		// mock dependencies with default values	
		lenient().when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(false); // By default user doesn't exits
		lenient().when(conversionServiceMock.convert(personPojoMock, Person.class)).thenReturn(personMock);		
		lenient().when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(false); // By default id doesn't exists
		lenient().when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
		
		// convertToUser
		lenient().when(passwordEncoderMock.encode(password)).thenReturn(password);
		lenient().when(rolesRepositoryMock.findByName(anyString())).thenReturn(customerRole);
	} 
	@Test
	void Everything_Correct_NoException() {		
		assertDoesNotThrow(() -> instance.register(regPojoMock));		
	}		
	
	@Test
	void NameAlreadyExists_EntityExistsException() {		
		when(usersRepositoryMock.exists(any(Predicate.class))).thenReturn(true);
		assertThrows(EntityExistsException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void IdAlreadyExists_EntityExistsException() {
		when(peopleRepositoryMock.exists(any(Predicate.class))).thenReturn(true);
		assertThrows(EntityExistsException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void CustomerRoleNotFound_IllegalStateException() {
		when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
		assertThrows(IllegalStateException.class, () -> instance.register(regPojoMock));
	}
	
}
