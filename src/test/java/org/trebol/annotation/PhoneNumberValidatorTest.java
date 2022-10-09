package org.trebol.annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.trebol.config.ValidationProperties;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberValidatorTest {
	
	@Mock
	private ValidationProperties validationProperties;
	
	@InjectMocks
	private PhoneNumberValidator phoneNumberValidator;
	
	private static String regex;
	
	@BeforeAll
	public static void beforeAll() {
		regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"; // todo to read from the application.properties
	}
	
	@BeforeEach
	public void beforeEach() {
		when(validationProperties.getPhoneNumberRegexp()).thenReturn(regex);
		phoneNumberValidator.initialize(null);
	}
	
	@Test
	public void when_InvalidPhoneNumber_ReturnFalse() {
		String phoneNumber = "+123";		
		assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
	}
	
	@Test
	public void when_ValidPhoneNumber_ReturnTrue() {
		String phoneNumber = "+123 123456";		
		assertTrue(phoneNumberValidator.isValid(phoneNumber, null));
	}
}
