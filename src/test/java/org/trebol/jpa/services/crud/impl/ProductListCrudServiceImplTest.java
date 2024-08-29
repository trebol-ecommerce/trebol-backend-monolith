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

package org.trebol.jpa.services.crud.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductListPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.ProductListItemsRepository;
import org.trebol.jpa.repositories.ProductListsRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListCrudServiceImplTest {
    @InjectMocks ProductListsCrudServiceImpl instance;
    @Mock ProductListsRepository productListRepositoryMock;
    @Mock ProductListItemsRepository productListItemRepositoryMock;

    @Test
    void matches_productlist_from_name() throws BadInputException {
        ProductListPojo input = ProductListPojo.builder()
            .name(ANY)
            .build();
        ProductList expectedResult = ProductList.builder().build();
        when(productListRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));
        Optional<ProductList> result = instance.getExisting(input);
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get());
        verify(productListRepositoryMock).findByName(input.getName());
    }

    @Test
    void cannot_match_any_productlist_from_null_data() {
        ProductListPojo input = ProductListPojo.builder().build();
        BadInputException result = assertThrows(BadInputException.class, () -> instance.getExisting(input));
        assertEquals("The specified list has no name", result.getMessage());
    }

    @Test
    void deletes_lists() {
        List<ProductList> productListsMock = List.of(
            ProductList.builder()
                .id(1L)
                .build()
        );
        when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(1L);
        when(productListRepositoryMock.findAll(any(Predicate.class))).thenReturn(productListsMock);
        instance.delete(new BooleanBuilder());
        verify(productListItemRepositoryMock, times(productListsMock.size())).deleteByListId(1L);
        verify(productListRepositoryMock).deleteAll(productListsMock);
    }

    @Test
    void attempting_to_delete_nothing_throws_EntityNotFoundException() {
        BooleanBuilder input = new BooleanBuilder();
        when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(0L);
        assertThrows(EntityNotFoundException.class, () -> instance.delete(input));
    }
}
