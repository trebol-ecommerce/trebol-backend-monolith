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

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.integration.exceptions.MailingServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.operation.DataCrudGenericController;
import org.trebol.operation.services.PaginationService;
import org.trebol.operation.services.SalesProcessService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/sales")
@PreAuthorize("isAuthenticated()")
public class DataSalesController
  extends DataCrudGenericController<SellPojo, Sell> {
  private final SalesProcessService processService;
  @Nullable
  private final IMailingIntegrationService mailingIntegrationService;

  @Autowired
  public DataSalesController(
    PaginationService paginationService,
    SortSpecService<Sell> sortService,
    SalesCrudService crudService,
    PredicateService<Sell> predicateService,
    SalesProcessService processService,
    @Autowired(required = false) IMailingIntegrationService mailingIntegrationService
  ) {
    super(paginationService, sortService, crudService, predicateService);
    this.processService = processService;
    this.mailingIntegrationService = mailingIntegrationService;
  }

  @Override
  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public DataPagePojo<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    if (allRequestParams != null) {
      if (allRequestParams.containsKey("buyOrder")) {
        Predicate predicate = predicateService.parseMap(allRequestParams);
        SellPojo sellPojo = crudService.readOne(predicate);
        return new DataPagePojo<>(List.of(sellPojo), 0, 1, 1);
      }
      if (!allRequestParams.containsKey("sortBy") && !allRequestParams.containsKey("order")) {
        allRequestParams = new HashMap<>(allRequestParams);
        allRequestParams.put("sortBy", "buyOrder");
        allRequestParams.put("order", "desc");
      }
    }
    return super.readMany(allRequestParams);
  }

  @Override
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:create')")
  public void create(@Valid @RequestBody SellPojo input)
    throws BadInputException, EntityExistsException {
    super.create(input);
  }

  @Override
  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void update(@RequestBody SellPojo input, @RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    super.update(input, requestParams);
  }

  @Override
  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:delete')")
  public void delete(@RequestParam Map<String, String> requestParams)
    throws EntityNotFoundException {
    super.delete(requestParams);
  }

  @PostMapping({"/confirmation", "/confirmation/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void confirmSell(@RequestBody SellPojo sell)
    throws BadInputException, MailingServiceException {
    SellPojo updatedSell = processService.markAsConfirmed(sell);
    if (this.mailingIntegrationService != null) {
      mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
      mailingIntegrationService.notifyOrderStatusToOwners(updatedSell);
    }
  }

  @PostMapping({"/rejection", "/rejection/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void rejectSell(@RequestBody SellPojo sell)
    throws BadInputException, MailingServiceException {
    SellPojo updatedSell = processService.markAsRejected(sell);
    if (this.mailingIntegrationService != null) {
      mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
    }
  }

  @PostMapping({"/completion", "/completion/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void completeSell(@RequestBody SellPojo sell)
    throws BadInputException, MailingServiceException {
    SellPojo updatedSell = processService.markAsCompleted(sell);
    if (this.mailingIntegrationService != null) {
      mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
    }
  }
}
