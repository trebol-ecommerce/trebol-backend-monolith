package org.trebol.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.config.CustomProperties;
import org.trebol.api.services.CatalogService;

@RestController
@RequestMapping("/store/products")
public class StoreFrontController {

  private final CatalogService catalogService;
  private final CustomProperties customProperties;

  @Autowired
  public StoreFrontController(CatalogService catalogService, CustomProperties customProperties) {
    this.catalogService = catalogService;
    this.customProperties = customProperties;
  }

  @GetMapping({"", "/"})
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    Integer requestPageSize = customProperties.getItemsPerPage();
    return catalogService.readProducts(requestPageSize, 0, allRequestParams);
  }
}
