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

import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.ProductListItemsRepository;
import org.trebol.jpa.repositories.ProductListsRepository;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.conversion.ProductListItemsConverterService;
import org.trebol.jpa.services.crud.ProductsCrudService;
import org.trebol.jpa.services.predicates.ProductListItemsPredicateService;
import org.trebol.jpa.sortspecs.ProductListItemsSortSpec;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/data/product_list_contents")
public class DataProductListContentsController {
  private static final String ITEM_NOT_FOUND = "Requested item(s) not found";
  private final PaginationService paginationService;
  private final SortSpecParserService sortService;
  private final ProductListItemsRepository listItemsRepository;
  private final ProductListsRepository listsRepository;
  private final ProductListItemsPredicateService listItemsPredicateService;
  private final ProductsCrudService productCrudService;
  private final ProductListItemsConverterService itemConverterService;

  @Autowired
  public DataProductListContentsController(
    PaginationService paginationService,
    SortSpecParserService sortService,
    ProductListItemsRepository listItemsRepository,
    ProductListsRepository listsRepository,
    ProductListItemsPredicateService listItemsPredicateService,
    ProductsCrudService productCrudService,
    ProductListItemsConverterService itemConverterService
  ) {
    this.paginationService = paginationService;
    this.sortService = sortService;
    this.listItemsRepository = listItemsRepository;
    this.listsRepository = listsRepository;
    this.listItemsPredicateService = listItemsPredicateService;
    this.productCrudService = productCrudService;
    this.itemConverterService = itemConverterService;
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductPojo> readContents(@RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    Optional<ProductList> match = this.fetchProductListByCode(requestParams);
    if (match.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    int pageIndex = paginationService.determineRequestedPageIndex(requestParams);
    int pageSize = paginationService.determineRequestedPageSize(requestParams);

    Pageable pagination;
    if (requestParams.containsKey("sortBy")) {
      Sort order = sortService.parse(ProductListItemsSortSpec.ORDER_SPEC_MAP, requestParams);
      pagination = PageRequest.of(pageIndex, pageSize, order);
    } else {
      pagination = PageRequest.of(pageIndex, pageSize);
    }

    Predicate predicate = listItemsPredicateService.parseMap(requestParams);
    Page<ProductListItem> listItems = listItemsRepository.findAll(predicate, pagination);
    List<ProductPojo> products = new ArrayList<>();
    for (ProductListItem item : listItems) {
      ProductPojo productPojo = itemConverterService.convertToPojo(item);
      products.add(productPojo);
    }
    long totalCount = listItemsRepository.count(QProductListItem.productListItem.list.id.eq(match.get().getId()));

    return new DataPagePojo<>(products, pageIndex, totalCount, pageSize);
  }

  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void addToContents(@Valid @RequestBody ProductPojo input,
                            @RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    Optional<Product> productMatch = productCrudService.getExisting(input);
    if (productMatch.isPresent()) {
      ProductListItem listItem = ProductListItem.builder()
        .list(listMatch.get())
        .product(productMatch.get())
        .build();
      if (!listItemsRepository.exists(Example.of(listItem))) {
        listItemsRepository.save(listItem);
      }
    }
  }

  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void updateContents(@RequestBody Collection<ProductPojo> input,
                             @RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    listItemsRepository.deleteByListId(listMatch.get().getId());
    for (ProductPojo p : input) {
      Optional<Product> productMatch = productCrudService.getExisting(p);
      if (productMatch.isPresent()) {
        ProductListItem listItem = ProductListItem.builder()
          .list(listMatch.get())
          .product(productMatch.get())
          .build();
        if (!listItemsRepository.exists(Example.of(listItem))) {
          listItemsRepository.save(listItem);
        }
      }
    }
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void deleteFromContents(@RequestParam Map<String, String> requestParams)
    throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    Predicate predicate = listItemsPredicateService.parseMap(requestParams);
    listItemsRepository.deleteAll(listItemsRepository.findAll(predicate));
  }

  private Optional<ProductList> fetchProductListByCode(Map<String, String> requestParams) throws BadInputException {
    String listCode = requestParams.get("listCode");
    if (StringUtils.isBlank(listCode)) {
      throw new BadInputException("listCode query param is required");
    }
    return listsRepository.findOne(QProductList.productList.code.eq(listCode));
  }
}
