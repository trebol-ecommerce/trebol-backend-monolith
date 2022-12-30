package org.trebol.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.trebol.annotation.validator.PhoneNumberValidator;
import org.trebol.config.ValidationProperties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
class PhoneNumberValidatorTest {

  @Mock
  private ValidationProperties validationProperties;

  @Value("${trebol.validation.phonenumber-regexp}")
  private String phoneNumberRegex;

  @InjectMocks
  private PhoneNumberValidator phoneNumberValidator;

  private String phoneNumber;

  @BeforeEach
  void beforeEach() {
    phoneNumber = "";
    when(validationProperties.getPhoneNumberRegexp()).thenReturn(phoneNumberRegex);
    phoneNumberValidator.initialize(null);
  }

  @DisplayName("Validate invalid phone number w/ no country code, only country code, and over 15 digits " +
    "it should return false")
  @ParameterizedTest
  @ValueSource(strings = {"123456", "+123","+123 4567890123456"})
  void testPhoneValidatorWithInvalidPhoneNumber(String phoneNumber) {
    assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
  }

  @DisplayName("Validate phone number with or without space and return true")
  @ParameterizedTest
  @ValueSource(strings = {"+123456789", "+123 456789"})
  void testPhoneValidatorWithOrWithoutSpace(String phoneNumber) {
    assertTrue(phoneNumberValidator.isValid(phoneNumber, null));
  }
}
