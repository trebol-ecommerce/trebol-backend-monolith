/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.common.annotations;

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
import org.trebol.common.annotations.validator.PhoneNumberValidator;
import org.trebol.config.ValidationProperties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
class PhoneNumberValidatorTest {
  @InjectMocks PhoneNumberValidator phoneNumberValidator;
  @Mock ValidationProperties validationPropertiesMock;
  @Value("${trebol.validation.phonenumber-regexp}")
  String phoneNumberRegex;
  String phoneNumber;

  @BeforeEach
  void beforeEach() {
    phoneNumber = "";
    when(validationPropertiesMock.getPhoneNumberRegexp()).thenReturn(phoneNumberRegex);
    phoneNumberValidator.initialize(null);
  }

  @DisplayName("Validate invalid phone number w/ no country code, only country code, and over 15 digits " +
    "it should return false")
  @ParameterizedTest
  @ValueSource(strings = {"123456", "+123", "+123 4567890123456"})
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
