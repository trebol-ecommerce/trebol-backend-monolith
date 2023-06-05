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

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.services.CrudService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecParserService;

import java.util.Map;

/**
 * Base class that implements {@link org.trebol.api.DataController}.<br/>
 * Thus, it can only read data of a given type.
 *
 * @param <M> The model class
 * @param <E> The entity class
 */
public abstract class DataGenericController<M, E>
  implements DataController<M> {
  protected final PaginationService paginationService;
  protected final SortSpecParserService sortService;
  protected final CrudService<M, E> crudService;
  protected final PredicateService predicateService;

  protected abstract Map<String, OrderSpecifier<?>> getOrderSpecMap();

  protected DataGenericController(
    PaginationService paginationService,
    SortSpecParserService sortService,
    CrudService<M, E> crudService,
    PredicateService predicateService
  ) {
    this.paginationService = paginationService;
    this.sortService = sortService;
    this.crudService = crudService;
    this.predicateService = predicateService;
  }

  /**
   * Retrieve a page of items with a fixed size and offset index.
   * An optional Map (like query string parameters) can be provided for filtering criteria
   *
   * @param requestParams May contain filtering conditions and/or page size & page index parameters.
   * @return A paged collection of Pojos.
   */
  @Override
  public DataPagePojo<M> readMany(@Nullable Map<String, String> requestParams) {
    int pageIndex = paginationService.determineRequestedPageIndex(requestParams);
    int pageSize = paginationService.determineRequestedPageSize(requestParams);

    Sort order = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      order = sortService.parse(getOrderSpecMap(), requestParams);
    }

    Predicate filters = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      filters = predicateService.parseMap(requestParams);
    }

    return crudService.readMany(pageIndex, pageSize, order, filters);
  }
}
