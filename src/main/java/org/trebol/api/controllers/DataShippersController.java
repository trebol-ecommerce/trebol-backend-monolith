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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.DataCrudGenericController;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.ShipperPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.ShippersCrudService;
import org.trebol.jpa.services.predicates.ShippersPredicateService;
import org.trebol.jpa.sortspecs.ShippersSortSpec;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/data/shippers")
@Tag(name = "Shippers management")
public class DataShippersController
    extends DataCrudGenericController<ShipperPojo, Shipper> {

    @Autowired
    public DataShippersController(
        PaginationService paginationService,
        SortSpecParserService sortService,
        ShippersCrudService crudService,
        ShippersPredicateService predicateService
    ) {
        super(paginationService, sortService, crudService, predicateService);
    }

    @Override
    @GetMapping
    @Operation(summary = "List shippers.")
    public DataPagePojo<ShipperPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
        return super.readMany(allRequestParams);
    }

    @Override
    @PostMapping
    @Operation(summary = "Define new shippers.")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('shippers:create')")
    public void create(@Valid @RequestBody ShipperPojo input)
        throws BadInputException, EntityExistsException {
        crudService.create(input);
    }

    @Override
    @PutMapping
    @Operation(summary = "Replace shippers data.")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('shippers:update')")
    public void update(@Valid @RequestBody ShipperPojo input, @RequestParam Map<String, String> requestParams)
        throws BadInputException, EntityNotFoundException {
        super.update(input, requestParams);
    }

    @Override
    @PatchMapping
    @Operation(summary = "Update parts of shippers data.")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('shippers:update')")
    public void partialUpdate(
        @RequestBody Map<String, Object> input,
        @RequestParam Map<String, String> requestParams
    ) throws BadInputException, EntityNotFoundException {
        super.partialUpdate(input, requestParams);
    }

    @Override
    @DeleteMapping
    @Operation(summary = "Remove shippers.")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('shippers:delete')")
    public void delete(@RequestParam Map<String, String> requestParams)
        throws EntityNotFoundException {
        super.delete(requestParams);
    }

    @Override
    protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
        return ShippersSortSpec.ORDER_SPEC_MAP;
    }
}
