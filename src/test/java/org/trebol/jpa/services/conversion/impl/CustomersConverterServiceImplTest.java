/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.conversion.PeopleConverterService;
import org.trebol.testing.CustomersTestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomersConverterServiceImplTest {
    @InjectMocks CustomersConverterServiceImpl instance;
    @Mock PeopleConverterService peopleServiceMock;
    final CustomersTestHelper customersTestHelper = new CustomersTestHelper();

    @BeforeEach
    void beforeEach() {
        customersTestHelper.resetCustomers();
    }

    @Test
    void testConvertToPojo() {
        Customer input = customersTestHelper.customerEntityAfterCreation();
        PersonPojo expectedPersonPojo = PersonPojo.builder().build();
        when(peopleServiceMock.convertToPojo(any(Person.class))).thenReturn(expectedPersonPojo);
        PersonPojo result = instance.convertToPojo(input);
        assertNotNull(result);
        assertEquals(expectedPersonPojo, result);
    }

    @Test
    void testConvertToNewEntity() throws BadInputException {
        PersonPojo customerPojo = customersTestHelper.customerPojoBeforeCreation();
        Person expectedPerson = Person.builder().build();
        when(peopleServiceMock.convertToNewEntity(any(PersonPojo.class))).thenReturn(expectedPerson);
        Customer result = instance.convertToNewEntity(customerPojo);
        assertNotNull(result);
        assertEquals(expectedPerson, result.getPerson());
    }
}
