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

package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class SalespeopleDataTransportServiceImplTest {
  @InjectMocks SalespeopleDataTransportServiceImpl sut;
  @Mock PeopleDataTransportService peopleDataTransportService;
  Salesperson salesperson;
  SalespersonPojo salespersonPojo;
  Person person;

  @BeforeEach
  void beforeEach() {
    person = new Person();
    person.setId(ID_1L);
    salesperson = new Salesperson();
    salesperson.setId(ID_1L);
    salesperson.setPerson(person);
    salespersonPojo = SalespersonPojo.builder()
      .person(PersonPojo.builder().id(ID_1L).build())
      .build();
  }


  @Test
  void testApplyChangesToExistingEntityThrowsBadInputException() {
    salespersonPojo.setPerson(null);
    BadInputException badInputException = assertThrows(BadInputException.class, ()
      -> sut.applyChangesToExistingEntity(salespersonPojo, salesperson));
    assertEquals("Salesperson must have a person profile", badInputException.getMessage());
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    when(peopleDataTransportService.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(person);
    Salesperson actual = sut.applyChangesToExistingEntity(salespersonPojo, salesperson);
    assertEquals(person.getId(), actual.getPerson().getId());
  }
}
