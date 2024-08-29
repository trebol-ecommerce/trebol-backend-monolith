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


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.ProductsCategoriesRepository;
import org.trebol.jpa.services.patch.ProductCategoriesPatchService;
import org.trebol.testing.ProductCategoriesTestHelper;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesCrudServiceImplTest {
    @InjectMocks ProductCategoriesCrudServiceImpl instance;
    @Mock ProductsCategoriesRepository categoriesRepositoryMock;
    @Mock ProductCategoriesPatchService categoriesPatchServiceMock;
    final ProductCategoriesTestHelper categoriesHelper = new ProductCategoriesTestHelper();
    static ProductCategory EXISTING_ENTITY;

    @BeforeAll
    static void beforeAll() {
        EXISTING_ENTITY = ProductCategory.builder()
            .id(2L)
            .name(ANY)
            .code(ANY)
            .parent(ProductCategory.builder()
                .id(1L)
                .build())
            .build();
    }

    @BeforeEach
    void beforeEach() {
        categoriesHelper.resetProductCategories();
    }

    @Test
    void finds_by_code() throws BadInputException {
        ProductCategoryPojo input = ProductCategoryPojo.builder()
            .code(ANY)
            .build();
        ProductCategory expectedResult = ProductCategory.builder().build();
        when(categoriesRepositoryMock.findByCode(anyString())).thenReturn(Optional.of(expectedResult));

        Optional<ProductCategory> match = instance.getExisting(input);

        verify(categoriesRepositoryMock).findByCode(ANY);
        assertTrue(match.isPresent());
        assertEquals(expectedResult, match.get());
    }

    @Test
    void does_not_perform_queries_with_empty_codes() throws BadInputException {
        ProductCategoryPojo input = ProductCategoryPojo.builder().build();

        BadInputException result = assertThrows(BadInputException.class, () -> instance.getExisting(input));
        assertEquals("Invalid category code", result.getMessage());
    }

    @Test
    void partially_updates_using_patch_service() throws BadInputException {
        Map<String, Object> changes = Map.of();
        ProductCategory patchedEntity = categoriesHelper.productCategoryEntityBeforeCreation();
        when(categoriesPatchServiceMock.patchExistingEntity(anyMap(), any(ProductCategory.class))).thenReturn(patchedEntity);
        when(categoriesRepositoryMock.saveAndFlush(any(ProductCategory.class))).thenReturn(patchedEntity);

        ProductCategory result = instance.flushPartialChanges(changes, EXISTING_ENTITY);

        assertNotEquals(EXISTING_ENTITY, result);
        assertEquals(patchedEntity, result);
        verify(categoriesPatchServiceMock).patchExistingEntity(changes, EXISTING_ENTITY);
        verify(categoriesRepositoryMock).saveAndFlush(patchedEntity);
    }

    @Test
    void returns_the_input_object_when_no_partial_changes_are_detected() throws BadInputException {
        Map<String, Object> changes = Map.of();
        ProductCategory preexistingEntityWithoutParent = categoriesHelper.productCategoryEntityAfterCreation();
        when(categoriesPatchServiceMock.patchExistingEntity(anyMap(), any(ProductCategory.class))).thenReturn(preexistingEntityWithoutParent);

        ProductCategory result = instance.flushPartialChanges(changes, preexistingEntityWithoutParent);

        assertEquals(preexistingEntityWithoutParent, result);
        verify(categoriesPatchServiceMock).patchExistingEntity(changes, preexistingEntityWithoutParent);
    }
}
