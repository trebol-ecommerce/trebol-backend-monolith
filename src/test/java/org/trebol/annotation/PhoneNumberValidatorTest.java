package org.trebol.annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.trebol.annotation.validator.PhoneNumberValidator;
import org.trebol.config.ValidationProperties;


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

  @Test
  void when_OnlyCountryCode_ReturnFalse() {
    phoneNumber = "+123";
    assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
  }

  @Test
  void when_CountryCodeIsMissing_ReturnFalse() {
    phoneNumber = "123456";
    assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
  }

  @Test
  void when_WithoutSpace_ReturnTrue() {
    phoneNumber = "+123456789";
    assertTrue(phoneNumberValidator.isValid(phoneNumber, null));
  }

  @Test
  void when_WithSpace_ReturnTrue() {
    phoneNumber = "+123 456789";
    assertTrue(phoneNumberValidator.isValid(phoneNumber, null));
  }

  @Test
  void when_Over15Digits_ReturnFalse() {
    phoneNumber = "+123 4567890123456";
    assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
  }
}
