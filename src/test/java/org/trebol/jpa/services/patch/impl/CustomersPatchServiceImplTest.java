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
import org.trebol.jpa.services.patch.PeoplePatchService;
import org.trebol.testing.PeopleTestHelper;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.PERSON_DATA_MAP_KEYS_PREFIX;

@ExtendWith(MockitoExtension.class)
class CustomersPatchServiceImplTest {
    @InjectMocks CustomersPatchServiceImpl instance;
    @Mock PeoplePatchService peoplePatchServiceMock;
    PeopleTestHelper peopleTestHelper = new PeopleTestHelper();
    private static ObjectMapper MAPPER;
    private static Customer EXISTING_CUSTOMER;

    @BeforeAll
    static void beforeAll() {
        MAPPER = new ObjectMapper();
        EXISTING_CUSTOMER = Customer.builder()
            .id(1L)
            .person(Person.builder().build())
            .build();
    }

    @BeforeEach
    void beforeEach() {
        peopleTestHelper.resetPeople();
    }

    @Test
    void delegates_patching_to_peoplePatchService() throws BadInputException {
        PersonPojo somePersonPojo = peopleTestHelper.personPojoBeforeCreation();
        Map<String, Object> input = this.mapFrom(somePersonPojo);
        Map<String, Object> serviceInput = this.nestPersonDataMap(input);
        Person expectedPerson = peopleTestHelper.personEntityAfterCreation();
        when(peoplePatchServiceMock.patchExistingEntity(anyMap(), any(Person.class))).thenReturn(expectedPerson);
        Customer result = instance.patchExistingEntity(serviceInput, EXISTING_CUSTOMER);
        assertEquals(expectedPerson, result.getPerson());
        verify(peoplePatchServiceMock).patchExistingEntity(input, EXISTING_CUSTOMER.getPerson());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapFrom(PersonPojo data) {
        return MAPPER.convertValue(data, Map.class);
    }

    private Map<String, Object> nestPersonDataMap(Map<String, Object> data) {
        return data.entrySet().stream()
            .map(entry -> Map.entry(
                PERSON_DATA_MAP_KEYS_PREFIX + entry.getKey(),
                entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
