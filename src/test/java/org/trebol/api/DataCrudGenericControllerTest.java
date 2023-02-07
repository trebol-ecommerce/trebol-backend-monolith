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

package org.trebol.api;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.lang.Nullable;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.CrudService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecParserService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.trebol.testing.TestConstants.ANY;

public abstract class DataCrudGenericControllerTest<P, E>
  extends DataGenericControllerTest<P, E> {
  protected DataCrudGenericController<P, E> instance;
  protected PaginationService paginationServiceMock;
  protected SortSpecParserService sortServiceMock;
  protected CrudService<P, E> crudServiceMock;
  protected PredicateService predicateServiceMock;

  protected void beforeEach() {
    super.instance = instance;
    super.crudServiceMock = crudServiceMock;
  }

  protected void creates_data(P input) throws BadInputException {
    instance.create(input);

    verify(crudServiceMock).create(input);
  }

  protected void updates_data_using_only_a_pojo(P input) throws BadInputException {
    instance.update(input, Map.of());

    verify(crudServiceMock).update(input);
  }

  protected void updates_data_parsing_predicate_filters_from_map(P input, @Nullable Map<String, String> predicateFiltersMap) throws BadInputException {
    if (predicateFiltersMap == null) {
      predicateFiltersMap = Map.of(ANY, ANY);
    }
    Predicate predicate = new BooleanBuilder();
    when(predicateServiceMock.parseMap(anyMap())).thenReturn(predicate);

    instance.update(input, predicateFiltersMap);

    verify(crudServiceMock).update(input, predicate);
  }

  protected void deletes_data_parsing_predicate_filters_from_map(@Nullable Map<String, String> predicateFiltersMap) {
    if (predicateFiltersMap == null) {
      predicateFiltersMap = Map.of(ANY, ANY);
    }
    Predicate predicate = new BooleanBuilder();
    when(predicateServiceMock.parseMap(anyMap())).thenReturn(predicate);

    instance.delete(predicateFiltersMap);

    verify(predicateServiceMock).parseMap(predicateFiltersMap);
    verify(crudServiceMock).delete(predicate);
  }

  protected void does_not_delete_data_when_predicate_filters_map_is_empty() {
    instance.delete(Map.of());
    verify(crudServiceMock, never()).delete(any(Predicate.class));
  }
}
