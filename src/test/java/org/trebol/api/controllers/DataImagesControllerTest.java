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
import org.trebol.api.models.ImagePojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.ImagesCrudService;
import org.trebol.jpa.services.predicates.ImagesPredicateService;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataImagesControllerTest
    extends DataCrudGenericControllerTest<ImagePojo, Image> {
    @InjectMocks DataImagesController instance;
    @Mock PaginationService paginationServiceMock;
    @Mock SortSpecParserService sortServiceMock;
    @Mock ImagesCrudService crudServiceMock;
    @Mock ImagesPredicateService predicateServiceMock;

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
    void reads_images() {
        assertDoesNotThrow(() -> {
            super.reads_data(null);
            super.reads_data(Map.of());
            super.reads_data(Map.of(ANY, ANY));
        });
    }

    @Test
    void creates_images() {
        assertDoesNotThrow(() -> {
            ImagePojo input = ImagePojo.builder()
                .url(ANY)
                .filename(ANY)
                .code(ANY)
                .build();
            super.creates_data(input);
        });
    }

    @Test
    void updates_images_using_predicate_filters_map() {
        assertDoesNotThrow(() -> {
            ImagePojo existingList = ImagePojo.builder().build();
            ImagePojo input = ImagePojo.builder()
                .url(ANY)
                .filename(ANY)
                .code(ANY)
                .build();
            Predicate predicate = new BooleanBuilder();
            when(predicateServiceMock.parseMap(anyMap())).thenReturn(predicate);
            when(crudServiceMock.update(any(), any(Predicate.class))).thenReturn(Optional.of(existingList));

            instance.update(input, Map.of(ANY, ANY));

            verify(crudServiceMock).update(input, predicate);
        });
    }

    @Test
    void deletes_images() {
        assertDoesNotThrow(() -> super.deletes_data_parsing_predicate_filters_from_map(Map.of(ANY, ANY)));
    }

    @Test
    void does_not_delete_images_when_predicate_filters_map_is_empty() {
        assertDoesNotThrow(super::does_not_delete_data_when_predicate_filters_map_is_empty);
    }
}
