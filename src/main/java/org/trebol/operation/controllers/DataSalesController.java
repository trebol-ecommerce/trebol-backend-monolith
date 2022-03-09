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

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.integration.exceptions.MailingServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortJpaService;
import org.trebol.operation.GenericDataCrudController;
import org.trebol.operation.ISalesProcessService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/sales")
@PreAuthorize("isAuthenticated()")
public class DataSalesController
  extends GenericDataCrudController<SellPojo, Sell> {

  private final ISortJpaService<Sell> sortService;
  private final ISalesProcessService processService;
  private final IMailingIntegrationService mailingIntegrationService;

  @Autowired
  public DataSalesController(OperationProperties globals,
                             GenericCrudJpaService<SellPojo, Sell> crudService,
                             IPredicateJpaService<Sell> predicateService,
                             ISortJpaService<Sell> sortService,
                             ISalesProcessService processService,
                             IMailingIntegrationService mailingIntegrationService) {
    super(globals, crudService, predicateService);
    this.sortService = sortService;
    this.processService = processService;
    this.mailingIntegrationService = mailingIntegrationService;
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('sales:read')")
  public DataPagePojo<SellPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    if (allRequestParams.containsKey("buyOrder")) {
      Predicate predicate = predicateService.parseMap(allRequestParams);
      SellPojo sellPojo = crudService.readOne(predicate);
      return new DataPagePojo<>(List.of(sellPojo), 0, 1, 1);
    }
    if (!allRequestParams.containsKey("sortBy") && !allRequestParams.containsKey("order")) {
      allRequestParams.put("sortBy", "buyOrder");
      allRequestParams.put("order", "desc");
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
    // mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
    // mailingIntegrationService.notifyOrderStatusToOwners(updatedSell);
  }

  @PostMapping({"/rejection", "/rejection/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void rejectSell(@RequestBody SellPojo sell)
      throws BadInputException, MailingServiceException {
    SellPojo updatedSell = processService.markAsRejected(sell);
    // mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
  }

  @PostMapping({"/completion", "/completion/"})
  @PreAuthorize("hasAuthority('sales:update')")
  public void completeSell(@RequestBody SellPojo sell)
      throws BadInputException, MailingServiceException {
    SellPojo updatedSell = processService.markAsCompleted(sell);
    // mailingIntegrationService.notifyOrderStatusToClient(updatedSell);
  }

  @Override
  protected Sort determineSortOrder(Map<String, String> requestParams) {
    return sortService.parseMap(requestParams);
  }
}
