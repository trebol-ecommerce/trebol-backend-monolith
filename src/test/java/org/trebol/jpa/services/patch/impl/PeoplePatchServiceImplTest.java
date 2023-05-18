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

package org.trebol.jpa.services.patch.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.junit.jupiter.api.Assertions.*;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class PeoplePatchServiceImplTest {
  @InjectMocks PeoplePatchServiceImpl instance;
  private static ObjectMapper MAPPER;
  private static Person EXISTING_PERSON;

  @BeforeAll
  static void beforeAll() {
    MAPPER = new ObjectMapper();
    MAPPER.setSerializationInclusion(NON_NULL);
    EXISTING_PERSON = Person.builder()
      .id(1L)
      .idNumber(ANY)
      .firstName(ANY)
      .lastName(ANY)
      .email(ANY)
      .build();
  }

  @Test
  void performs_empty_patch() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder().build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertEquals(EXISTING_PERSON, result);
  }

  @Test
  void patches_idNumber() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder()
      .idNumber(NOT_ANY)
      .build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertNotEquals(EXISTING_PERSON, result);
    assertEquals(NOT_ANY, result.getIdNumber());
  }
  @Test
  void patches_names() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder()
      .firstName(NOT_ANY)
      .lastName(NOT_ANY)
      .build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertNotEquals(EXISTING_PERSON, result);
    assertEquals(NOT_ANY, result.getFirstName());
    assertEquals(NOT_ANY, result.getLastName());
  }

  @Test
  void patches_email() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder()
      .email(NOT_ANY)
      .build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertNotEquals(EXISTING_PERSON, result);
    assertEquals(NOT_ANY, result.getEmail());
  }

  @Test
  void patches_phones() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder()
      .phone1(NOT_ANY)
      .phone2(NOT_ANY)
      .build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertNotEquals(EXISTING_PERSON, result);
    assertEquals(NOT_ANY, result.getPhone1());
    assertEquals(NOT_ANY, result.getPhone2());
  }

  @Test
  void patches_all_fields() throws BadInputException {
    Map<String, Object> input = this.mapFrom(PersonPojo.builder()
      .idNumber(NOT_ANY)
      .firstName(NOT_ANY)
      .lastName(NOT_ANY)
      .email(NOT_ANY)
      .phone1(NOT_ANY)
      .phone2(NOT_ANY)
      .build());
    Person result = instance.patchExistingEntity(input, EXISTING_PERSON);
    assertNotEquals(EXISTING_PERSON, result);
    assertEquals(NOT_ANY, result.getIdNumber());
    assertEquals(NOT_ANY, result.getFirstName());
    assertEquals(NOT_ANY, result.getLastName());
    assertEquals(NOT_ANY, result.getEmail());
    assertEquals(NOT_ANY, result.getPhone1());
    assertEquals(NOT_ANY, result.getPhone2());
  }

  @Test
  void does_not_support_old_method_signature() {
    PersonPojo input = PersonPojo.builder().build();
    assertThrows(UnsupportedOperationException.class,
      () -> instance.patchExistingEntity(input, EXISTING_PERSON));
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> mapFrom(PersonPojo data) {
    return MAPPER.convertValue(data, Map.class);
  }
}
