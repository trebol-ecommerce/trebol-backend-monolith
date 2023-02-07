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

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.DataGenericController;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.PeopleCrudService;
import org.trebol.jpa.services.predicates.PeoplePredicateService;
import org.trebol.jpa.sortspecs.PeopleSortSpec;

import java.util.Map;

@RestController
@RequestMapping("/data/people")
@PreAuthorize("isAuthenticated()")
public class DataPeopleController
  extends DataGenericController<PersonPojo, Person> {

  @Autowired
  public DataPeopleController(
    PaginationService paginationService,
    SortSpecParserService sortService,
    PeopleCrudService crudService,
    PeoplePredicateService predicateService
  ) {
    super(paginationService, sortService, crudService, predicateService);
  }

  @Override
  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('people:read')")
  public DataPagePojo<PersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(allRequestParams);
  }

  @Override
  protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
    return PeopleSortSpec.ORDER_SPEC_MAP;
  }
}
