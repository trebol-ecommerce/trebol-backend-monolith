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

import com.querydsl.core.types.Predicate;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.CrudService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecParserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Map;

public abstract class DataCrudGenericController<P, E>
  extends DataGenericController<P, E>
  implements DataCrudController<P> {

  public DataCrudGenericController(
    PaginationService paginationService,
    SortSpecParserService sortSpecParserService,
    CrudService<P, E> crudService,
    PredicateService predicateService
  ) {
    super(paginationService, sortSpecParserService, crudService, predicateService);
  }

  @Override
  public void create(P input)
    throws BadInputException, EntityExistsException {
    crudService.create(input);
  }

  @Override
  public void update(P input, Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = predicateService.parseMap(requestParams);
      crudService.update(input, predicate);
    } else {
      crudService.update(input);
    }
  }

  @Override
  public void delete(Map<String, String> requestParams)
    throws EntityNotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = predicateService.parseMap(requestParams);
      crudService.delete(predicate);
    }
  }
}