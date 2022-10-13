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
import org.trebol.exceptions.BadInputException;
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
	void RegPojo_IsNull_BadInputException() {		
		assertThrows(BadInputException.class, () -> instance.register(null));
	}
	
	@Test
	void RegPojo_NameIsNull_BadInputException() {
		regPojoMock.setName(null);
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));		
	}
	
	@Test
	void RegPojo_NameIsBlank_BadInputException() {
		regPojoMock.setName("");
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void RegPojo_PasswordIsNull_BadInputException() {
		regPojoMock.setPassword(null);
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void RegPojo_PasswordIsBlank_BadInputException() {
		regPojoMock.setPassword("");
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void RegPojo_ProfileIsNull_BadInputException() {
		regPojoMock.setProfile(null);
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_FirstNameIsNull_BadInputException() {
		personPojoMock.setFirstName(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_FirstNameIsBlank_BadInputException() {
		personPojoMock.setFirstName("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_LastNameIsNull_BadInputException() {
		personPojoMock.setLastName(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_LastNameIsBlank_BadInputException() {
		personPojoMock.setLastName("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_IdNumberIsNull_BadInputException() {
		personPojoMock.setIdNumber(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_IdNumberIsBlank_BadInputException() {
		personPojoMock.setIdNumber("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_EmailIsNull_BadInputException() {
		personPojoMock.setEmail(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_EmailIsBlank_BadInputException() {
		personPojoMock.setEmail("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_Phone1IsNull_BadInputException() {
		personPojoMock.setPhone1(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_Phone1IsBlank_BadInputException() {
		personPojoMock.setPhone1("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_Phone2IsNull_BadInputException() {
		personPojoMock.setPhone2(null);		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
	}
	
	@Test
	void PersonPojo_Phone2IsBlank_BadInputException() {
		personPojoMock.setPhone2("");		
		assertThrows(BadInputException.class, () -> instance.register(regPojoMock));
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
