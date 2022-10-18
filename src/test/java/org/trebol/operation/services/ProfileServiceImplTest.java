package org.trebol.operation.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.exceptions.PersonNotFoundException;
import org.trebol.jpa.exceptions.UserNotFoundException;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
	
	@InjectMocks 	ProfileServiceImpl instance;
	@Mock	IUsersJpaRepository usersRepositoryMock;
	@Mock	GenericCrudJpaService<PersonPojo, Person> peopleServiceMock;
	@Mock	ITwoWayConverterJpaService<PersonPojo, Person> peopleConverterMock;
	@Mock	IPeopleJpaRepository peopleRepositoryMock;
	
	UserRole userRoleMock;
	Person personMock;
	User userMock;	
	PersonPojo personPojoMock;
	
	@BeforeEach
	void beforeEach() {
		// Default mock objects
		userRoleMock = new UserRole(1L, "roleName");
		personMock = new Person(1L, "firstName", "lastName", "123",
				"email@mock.com", "+123 456", "+123 456");
		userMock = new User(1L, "userName", "password", personMock, userRoleMock);
		personPojoMock = new PersonPojo(1L, "firstName", "lastName", "1", 
				"email@example.com", "+123 456", "+123 456");
	}
	
	// TEST METHOD: getProfileFromUserName(String userName)
	
	@Test
	void getProfileFromUserName_UserNotFound_UserNotFoundException() {		
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.empty()); // in getUserFromName
		
		assertThrows(UserNotFoundException.class, () -> instance.getProfileFromUserName("userName"));		
	}
	
	@Test
	void getProfileFromUserName_UserWithoutProfile_PersonNotFoundException() {
		userMock.setPerson(null); // Person profile is null
		
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock));
		
		assertThrows(PersonNotFoundException.class, () -> instance.getProfileFromUserName("userName"));
	}
	
	@Test
	void getProfileFromUserName_UserFoundWithProfile_NoException() {
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock)); // in getUserFromName
		
		assertDoesNotThrow(() -> instance.getProfileFromUserName("userName"));		
	}
	
	
	// TEST METHOD: updateProfileForUserWithName(String userName, PersonPojo profile)
	
	@Test
	void updateProfileForUserWithName_UserNotFound_UserNotFoundException() {
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.empty()); // in getUserFromName
		
		assertThrows(UserNotFoundException.class, 
				() -> instance.updateProfileForUserWithName("userName", null));		
	}
	
	@Test
	void updateProfileForUserWithName_UserHasProfile_UpdateProfile_NoException() throws BadInputException {
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock)); // in getUserFromName
		when(peopleConverterMock.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class)))
				.thenReturn(personMock);
		
		assertDoesNotThrow(() -> instance.updateProfileForUserWithName("userName", personPojoMock));
		
		verify(peopleRepositoryMock, times(1)).saveAndFlush(any(Person.class));
	}
	
	@Test
	void updateProfileForUserWithName_UserHasNoProfile_CreateNewProfile_NoException() throws BadInputException {		
		userMock.setPerson(null); // Person profile is null		
		
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock)); // in getUserFromName
		when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.empty());
		when(peopleConverterMock.convertToNewEntity(any(PersonPojo.class))).thenReturn(personMock);
		when(peopleRepositoryMock.saveAndFlush(any(Person.class))).thenReturn(personMock);
		when(usersRepositoryMock.saveAndFlush(any(User.class))).thenReturn(userMock);
		
		assertDoesNotThrow(() -> instance.updateProfileForUserWithName("userName", personPojoMock));
		
		verify(peopleRepositoryMock, times(1)).saveAndFlush(any(Person.class));
		verify(usersRepositoryMock, times(1)).saveAndFlush(any(User.class));		
	}	
	
	@Test
	void updateProfileForUserWithName_UserHasNoProfile_UseExistingProfile_NoException() throws BadInputException {
		userMock.setPerson(null); // Person profile is null		
		
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock)); // in getUserFromName
		when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.of(personMock));
		when(usersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.empty());
		when(usersRepositoryMock.saveAndFlush(any(User.class))).thenReturn(userMock);
		
		assertDoesNotThrow(() -> instance.updateProfileForUserWithName("userName", personPojoMock));
		
		verify(usersRepositoryMock, times(1)).saveAndFlush(any(User.class));
	}
	
	@Test
	void updateProfileForUserWithName_UserHasNoProfile_ExistingProfileAlreadyInUse_BadInputException() throws BadInputException {
		userMock.setPerson(null); // Person profile is null	
		
		when(usersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(userMock)); // in getUserFromName
		when(peopleServiceMock.getExisting(any(PersonPojo.class))).thenReturn(Optional.of(personMock));
		when(usersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(userMock));
		
		assertThrows(BadInputException.class, () -> instance.updateProfileForUserWithName("userName", personPojoMock));		
	}
	
}
