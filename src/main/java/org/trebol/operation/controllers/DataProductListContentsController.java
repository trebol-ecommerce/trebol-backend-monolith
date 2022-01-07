package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.entities.QProductList;
import org.trebol.jpa.entities.QProductListItem;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.operation.GenericPaginationController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/data/product_list_contents")
public class DataProductListContentsController
  extends GenericPaginationController {

  private static final String ITEM_NOT_FOUND = "Requested item(s) not found";

  private final IProductListItemsJpaRepository listItemsRepository;
  private final IProductListsJpaRepository listsRepository;
  private final IPredicateJpaService<ProductListItem> listItemsPredicateService;
  private final ITwoWayConverterJpaService<ProductPojo, ProductListItem> itemConverterService;

  @Autowired
  public DataProductListContentsController(OperationProperties globals,
                                           IProductListItemsJpaRepository listItemsRepository,
                                           IProductListsJpaRepository listsRepository,
                                           IPredicateJpaService<ProductListItem> listItemsPredicateService,
                                           ITwoWayConverterJpaService<ProductPojo,
                                           ProductListItem> itemConverterService) {
    super(globals);
    this.listItemsRepository = listItemsRepository;
    this.listsRepository = listsRepository;
    this.listItemsPredicateService = listItemsPredicateService;
    this.itemConverterService = itemConverterService;
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductPojo> readContents(@RequestParam Map<String, String> requestParams)
      throws BadInputException, NotFoundException {
    String listCode = requestParams.get("listCode");
    if (listCode == null || listCode.isBlank()) {
      throw new BadInputException("listCode query param is required");
    } else {
      Optional<ProductList> match = listsRepository.findOne(QProductList.productList.code.eq(listCode));
      if (match.isEmpty()) {
        throw new NotFoundException(ITEM_NOT_FOUND);
      } else {
        int pageIndex = super.determineRequestedPageIndex(requestParams);
        int pageSize = super.determineRequestedPageSize(requestParams);

        Pageable pagination;
        if (requestParams.containsKey("sortBy")) {
          Sort order = super.determineSortOrder(requestParams);
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
    }
  }

  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void addToContents(@Valid @RequestBody Collection<ProductPojo> input,
                            @RequestParam Map<String, String> requestParams) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void updateContents(@RequestBody Collection<ProductPojo> input,
                             @RequestParam Map<String, String> requestParams) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void deleteFromContents(@RequestParam Map<String, String> requestParams) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
