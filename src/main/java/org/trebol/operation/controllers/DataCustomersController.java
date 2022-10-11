/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.DataPagePojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/data/customers")
@PreAuthorize("isAuthenticated()")
public class DataCustomersController
  extends GenericDataCrudController<CustomerPojo, Customer> {

  @Autowired
  public DataCustomersController(PaginationService paginationService,
                                 ISortSpecJpaService<Customer> sortService,
                                 GenericCrudJpaService<CustomerPojo, Customer> crudService,
                                 IPredicateJpaService<Customer> predicateService) {
    super(paginationService, sortService, crudService, predicateService);
    this.sortService = sortService;
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:read')")
  public DataPagePojo<CustomerPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:create')")
  public void create(@Valid @RequestBody CustomerPojo input)
      throws BadInputException, EntityExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:update')")
  public void update(@RequestBody CustomerPojo input, @RequestParam Map<String, String> requestParams)
      throws EntityNotFoundException, BadInputException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('customers:delete')")
  public void delete(Map<String, String> requestParams)
      throws EntityNotFoundException {
    super.delete(requestParams);
  }
}
