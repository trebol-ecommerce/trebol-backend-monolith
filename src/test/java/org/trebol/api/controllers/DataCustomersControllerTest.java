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

package org.trebol.api.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.DataCrudGenericControllerTest;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.jpa.services.predicates.CustomersPredicateService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataCustomersControllerTest
    extends DataCrudGenericControllerTest<CustomerPojo, Customer> {
    @InjectMocks DataCustomersController instance;
    @Mock PaginationService paginationServiceMock;
    @Mock SortSpecParserService sortServiceMock;
    @Mock CustomersCrudService crudServiceMock;
    @Mock CustomersPredicateService predicateServiceMock;

    @Override
    @BeforeEach
    protected void beforeEach() {
        super.instance = instance;
        super.crudServiceMock = crudServiceMock;
        super.predicateServiceMock = predicateServiceMock;
        super.sortServiceMock = sortServiceMock;
        super.paginationServiceMock = paginationServiceMock;
        super.beforeEach();
    }

    @Test
    void reads_customers() {
        assertDoesNotThrow(() -> {
            super.reads_data(null);
            super.reads_data(Map.of());
            super.reads_data(Map.of(ANY, ANY));
        });
    }

    @Test
    void creates_customers() {
        assertDoesNotThrow(() -> super.creates_data(CustomerPojo.builder().build()));
    }

    @Test
    void updates_customers_using_predicate_filters_map() {
        assertDoesNotThrow(() -> {
            CustomerPojo existingList = CustomerPojo.builder().build();
            CustomerPojo input = CustomerPojo.builder().build();
            Predicate predicate = new BooleanBuilder();
            when(predicateServiceMock.parseMap(anyMap())).thenReturn(predicate);
            when(crudServiceMock.update(any(), any(Predicate.class))).thenReturn(Optional.of(existingList));

            instance.update(CustomerPojo.builder().build(), Map.of(ANY, ANY));

            verify(crudServiceMock).update(input, predicate);
        });
    }

    @Test
    void deletes_customers() {
        assertDoesNotThrow(() -> super.deletes_data_parsing_predicate_filters_from_map(Map.of(ANY, ANY)));
    }

    @Test
    void does_not_delete_customers_when_predicate_filters_map_is_empty() {
        assertDoesNotThrow(super::does_not_delete_data_when_predicate_filters_map_is_empty);
    }
}
