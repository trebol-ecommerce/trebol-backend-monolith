package org.trebol.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductFamilyPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.ProductTypePojo;
import org.trebol.config.CustomProperties;
import org.trebol.api.services.CatalogService;

@RestController
@RequestMapping("/store")
public class StoreProductsController {

  private final CatalogService catalogService;
  private final CustomProperties customProperties;

  @Autowired
  public StoreProductsController(CatalogService catalogService, CustomProperties customProperties) {
    this.catalogService = catalogService;
    this.customProperties = customProperties;
  }

  @GetMapping("/product/{id}")
  public ProductPojo readOne(@PathVariable Integer id) {
    return catalogService.readProduct(id);
  }

  @GetMapping("/front")
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    Integer requestPageSize = customProperties.getItemsPerPage();
    return catalogService.readProducts(requestPageSize, 0, allRequestParams);
  }

//  TODO Implement these two with query params
//
//  @GetMapping("/products/{requestPageSize}")
//  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize,
//      @RequestParam Map<String, String> allRequestParams) {
//    return catalogService.readProducts(requestPageSize, 0, allRequestParams);
//  }
//
//  @GetMapping("/products/{requestPageSize}/{requestPageIndex}")
//  public Collection<ProductPojo> readMany(@PathVariable Integer requestPageSize, @PathVariable Integer requestPageIndex,
//      @RequestParam Map<String, String> allRequestParams) {
//    return catalogService.readProducts(requestPageSize, requestPageIndex, allRequestParams);
//  }

  @GetMapping("/categories")
  public Collection<ProductFamilyPojo> readProductFamilies() {
    return catalogService.readProductFamilies();
  }

  @GetMapping("/categories/{familyId}")
  public Collection<ProductTypePojo> readProductTypesByFamilyId(@PathVariable Integer familyId) {
    if (familyId == null) {
      return catalogService.readProductTypes();
    } else {
      return catalogService.readProductTypesByFamilyId(familyId);
    }
  }
}
