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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.DataGenericController;
import org.trebol.api.models.BillingTypePojo;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.BillingTypesCrudService;
import org.trebol.jpa.services.predicates.BillingTypesPredicateService;
import org.trebol.jpa.sortspecs.BillingTypesSortSpec;

import java.util.Map;

@RestController
@RequestMapping("/data/billing_types")
public class DataBillingTypesController
  extends DataGenericController<BillingTypePojo, BillingType> {

  @Autowired
  public DataBillingTypesController(
    PaginationService paginationService,
    SortSpecParserService sortSpecParserService,
    BillingTypesCrudService crudService,
    BillingTypesPredicateService predicateService
  ) {
    super(paginationService, sortSpecParserService, crudService, predicateService);
  }

  @Override
  @GetMapping({"", "/"})
  public DataPagePojo<BillingTypePojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(allRequestParams);
  }

  @Override
  protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
    return BillingTypesSortSpec.ORDER_SPEC_MAP;
  }
}
