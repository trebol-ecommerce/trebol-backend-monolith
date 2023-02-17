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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.patch.PeoplePatchService;
import org.trebol.testing.CustomersTestHelper;
import org.trebol.testing.PeopleTestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomersPatchServiceImplTest {
  @InjectMocks CustomersPatchServiceImpl instance;
  @Mock PeoplePatchService peopleServiceMock;
  CustomersTestHelper customersTestHelper = new CustomersTestHelper();
  PeopleTestHelper peopleTestHelper = new PeopleTestHelper();

  @BeforeEach
  void beforeEach() {
    customersTestHelper.resetCustomers();
    peopleTestHelper.resetPeople();
  }

  @Test
  void patches_entity_data() throws BadInputException {
    Customer existingCustomer = customersTestHelper.customerEntityAfterCreation();
    CustomerPojo input = CustomerPojo.builder()
      .person(PersonPojo.builder().build())
      .build();
    Person expectedPerson = peopleTestHelper.personEntityAfterCreation();
    when(peopleServiceMock.patchExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(expectedPerson);
    Customer actual = instance.patchExistingEntity(input, existingCustomer);
    assertEquals(expectedPerson, actual.getPerson());
  }
}
