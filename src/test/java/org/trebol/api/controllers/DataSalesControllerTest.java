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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.DataCrudGenericControllerTest;
import org.trebol.api.models.SellPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.api.services.SalesProcessService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.integration.services.MailingService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.jpa.services.sortspecs.SalesSortSpecService;

import java.util.Map;

import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataSalesControllerTest
  extends DataCrudGenericControllerTest<SellPojo, Sell> {
  @InjectMocks DataSalesController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SalesSortSpecService sortServiceMock;
  @Mock SalesCrudService crudServiceMock;
  @Mock PredicateService<Sell> predicateServiceMock;
  @Mock SalesProcessService salesProcessServiceMock;
  @Mock MailingService mailingServiceMock;

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
  void reads_sales() {
    super.reads_data(null);
    super.reads_data(Map.of());
    super.reads_data(Map.of(ANY, ANY));
  }

  @Test
  void creates_sales() throws BadInputException {
    super.creates_data(SellPojo.builder().build());
  }

  @Test
  void updates_sales() throws BadInputException {
    super.updates_data_using_only_a_pojo(SellPojo.builder().build());
  }

  @Test
  void updates_sales_using_predicate_filters_map() throws BadInputException {
    super.updates_data_parsing_predicate_filters_from_map(SellPojo.builder().build(), null);
  }

  @Test
  void deletes_sales() throws BadInputException {
    super.deletes_data_parsing_predicate_filters_from_map(Map.of(ANY, ANY));
  }

  @Test
  void does_not_delete_sales_when_predicate_filters_map_is_empty() {
    super.does_not_delete_data_when_predicate_filters_map_is_empty();
  }
}
