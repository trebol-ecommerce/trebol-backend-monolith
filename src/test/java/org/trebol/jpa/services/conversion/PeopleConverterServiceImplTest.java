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

package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.conversion.impl.PeopleConverterServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class PeopleConverterServiceImplTest {
  @InjectMocks PeopleConverterServiceImpl sut;
  Person person;
  PersonPojo personPojo;

  @BeforeEach
  void beforeEach() {
    person = new Person();
    person.setId(1L);
    personPojo = PersonPojo.builder().id(1L).build();
  }

  @AfterEach
  void afterEach() {
    person = null;
    personPojo = null;
  }

  @Test
  void testConvertToPojo() {
    person.setIdNumber(ANY);
    person.setFirstName(ANY);
    person.setLastName(ANY);
    person.setEmail(ANY);
    PersonPojo actual = sut.convertToPojo(person);
    assertEquals(person.getIdNumber(), actual.getIdNumber());
    assertEquals(person.getFirstName(), actual.getFirstName());
    assertEquals(person.getLastName(), actual.getLastName());
    assertEquals(person.getEmail(), actual.getEmail());
    assertEquals(person.getPhone1(), actual.getPhone1());
    assertEquals(person.getPhone2(), actual.getPhone2());
  }

  @Test
  void testConvertToNewEntity() {
    personPojo.setIdNumber(ANY);
    personPojo.setFirstName(ANY);
    personPojo.setLastName(ANY);
    personPojo.setEmail(ANY);
    personPojo.setPhone1("");
    personPojo.setPhone2("");
    Person actual = sut.convertToNewEntity(personPojo);
    assertEquals(personPojo.getIdNumber(), actual.getIdNumber());
    assertEquals(personPojo.getFirstName(), actual.getFirstName());
    assertEquals(personPojo.getLastName(), actual.getLastName());
    assertEquals(personPojo.getEmail(), actual.getEmail());
    assertEquals(personPojo.getPhone1(), actual.getPhone1());
    assertEquals(personPojo.getPhone2(), actual.getPhone2());
  }
}
