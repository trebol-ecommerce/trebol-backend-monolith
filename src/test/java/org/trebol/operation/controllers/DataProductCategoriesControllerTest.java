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

package org.trebol.operation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.crud.ProductCategoriesCrudService;
import org.trebol.jpa.services.sortspecs.ProductCategoriesSortSpecService;
import org.trebol.operation.DataCrudGenericControllerTest;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Map;

import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataProductCategoriesControllerTest
  extends DataCrudGenericControllerTest<ProductCategoryPojo, ProductCategory> {
  @InjectMocks DataProductCategoriesController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock ProductCategoriesSortSpecService sortServiceMock;
  @Mock ProductCategoriesCrudService crudServiceMock;
  @Mock PredicateService<ProductCategory> predicateServiceMock;

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
  void reads_categories() {
    super.reads_data(null);
    super.reads_data(Map.of());
    super.reads_data(Map.of(ANY, ANY));
  }

  @Test
  void creates_categories() throws BadInputException {
    super.creates_data(ProductCategoryPojo.builder().build());
  }

  @Test
  void updates_categories() throws BadInputException {
    super.updates_data_using_only_a_pojo(ProductCategoryPojo.builder().build());
  }

  @Test
  void updates_categories_using_predicate_filters_map() throws BadInputException {
    super.updates_data_parsing_predicate_filters_from_map(ProductCategoryPojo.builder().build(), null);
  }

  @Test
  void deletes_categories() throws BadInputException {
    super.deletes_data_parsing_predicate_filters_from_map(Map.of(ANY, ANY));
  }

  @Test
  void does_not_delete_categories_when_predicate_filters_map_is_empty() {
    super.does_not_delete_data_when_predicate_filters_map_is_empty();
  }
}
