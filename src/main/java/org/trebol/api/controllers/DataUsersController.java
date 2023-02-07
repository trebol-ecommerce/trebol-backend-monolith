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
import org.springframework.web.bind.annotation.*;
import org.trebol.api.DataCrudGenericController;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.UserPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.UsersCrudService;
import org.trebol.jpa.services.predicates.UsersPredicateService;
import org.trebol.jpa.sortspecs.UsersSortSpec;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/data/users")
@PreAuthorize("isAuthenticated()")
public class DataUsersController
  extends DataCrudGenericController<UserPojo, User> {

  @Autowired
  public DataUsersController(
    PaginationService paginationService,
    SortSpecParserService sortService,
    UsersCrudService crudService,
    UsersPredicateService predicateService
  ) {
    super(paginationService, sortService, crudService, predicateService);
  }

  @Override
  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:read')")
  public DataPagePojo<UserPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:create')")
  public void create(@Valid @RequestBody UserPojo input)
    throws BadInputException, EntityExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:update')")
  public void update(@RequestBody UserPojo input, @RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    super.update(input, requestParams);
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('users:delete')")
  public void delete(Principal principal, @RequestParam Map<String, String> requestParams)
    throws EntityNotFoundException, BadInputException {
    if (requestParams.containsKey("name") && requestParams.get("name").equals(principal.getName())) {
      throw new BadInputException("A user should not be able to delete their own account");
    }
    super.delete(requestParams);
  }

  @Override
  protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
    return UsersSortSpec.ORDER_SPEC_MAP;
  }
}
