/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.DataCrudGenericController;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.OrderPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.api.services.OrdersProcessService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Order;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.OrdersCrudService;
import org.trebol.jpa.services.predicates.OrdersPredicateService;
import org.trebol.jpa.sortspecs.OrdersSortSpec;
import org.trebol.mailing.MailingService;
import org.trebol.mailing.MailingServiceException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/orders")
@PreAuthorize("isAuthenticated()")
public class DataOrdersController
    extends DataCrudGenericController<OrderPojo, Order> {
    private final OrdersProcessService processService;
    @Nullable
    private final MailingService mailingService;

    @Autowired
    public DataOrdersController(
        PaginationService paginationService,
        SortSpecParserService sortService,
        OrdersCrudService crudService,
        OrdersPredicateService predicateService,
        OrdersProcessService processService,
        @Autowired(required = false) MailingService mailingService
    ) {
        super(paginationService, sortService, crudService, predicateService);
        this.processService = processService;
        this.mailingService = mailingService;
    }

    @Override
    @GetMapping({"", "/"})
    @PreAuthorize("hasAuthority('orders:read')")
    public DataPagePojo<OrderPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
        if (allRequestParams!=null) {
            if (allRequestParams.containsKey("buyOrder")) {
                Predicate predicate = predicateService.parseMap(allRequestParams);
                OrderPojo orderPojo = crudService.readOne(predicate);
                DataPagePojo<OrderPojo> singleItemPage = new DataPagePojo<>();
                singleItemPage.setItems(List.of(orderPojo));
                singleItemPage.setTotalCount(1);
                singleItemPage.setPageSize(1);
                return singleItemPage;
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
    @PreAuthorize("hasAuthority('orders:create')")
    public void create(@Valid @RequestBody OrderPojo input)
        throws BadInputException, EntityExistsException {
        crudService.create(input);
    }

    @Override
    @PutMapping({"", "/"})
    @PreAuthorize("hasAuthority('orders:update')")
    public void update(@Valid @RequestBody OrderPojo input, @RequestParam Map<String, String> requestParams)
        throws BadInputException, EntityNotFoundException {
        super.update(input, requestParams);
    }

    @Override
    @PatchMapping({"", "/"})
    @PreAuthorize("hasAuthority('orders:update')")
    public void partialUpdate(
        @RequestBody Map<String, Object> input,
        @RequestParam Map<String, String> requestParams
    ) throws BadInputException, EntityNotFoundException {
        super.partialUpdate(input, requestParams);
    }

    @Override
    @DeleteMapping({"", "/"})
    @PreAuthorize("hasAuthority('orders:delete')")
    public void delete(@RequestParam Map<String, String> requestParams)
        throws EntityNotFoundException {
        super.delete(requestParams);
    }

    @PostMapping({"/confirmation", "/confirmation/"})
    @PreAuthorize("hasAuthority('orders:update')")
    public void confirmSell(@RequestBody OrderPojo sell)
        throws BadInputException, MailingServiceException {
        OrderPojo updatedSell = processService.markAsConfirmed(sell);
        if (this.mailingService!=null) {
            mailingService.notifyOrderStatusToClient(updatedSell);
            mailingService.notifyOrderStatusToOwners(updatedSell);
        }
    }

    @PostMapping({"/rejection", "/rejection/"})
    @PreAuthorize("hasAuthority('orders:update')")
    public void rejectSell(@RequestBody OrderPojo sell)
        throws BadInputException, MailingServiceException {
        OrderPojo updatedSell = processService.markAsRejected(sell);
        if (this.mailingService!=null) {
            mailingService.notifyOrderStatusToClient(updatedSell);
        }
    }

    @PostMapping({"/completion", "/completion/"})
    @PreAuthorize("hasAuthority('orders:update')")
    public void completeSell(@RequestBody OrderPojo sell)
        throws BadInputException, MailingServiceException {
        OrderPojo updatedSell = processService.markAsCompleted(sell);
        if (this.mailingService!=null) {
            mailingService.notifyOrderStatusToClient(updatedSell);
        }
    }

    @Override
    protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
        return OrdersSortSpec.ORDER_SPEC_MAP;
    }
}
