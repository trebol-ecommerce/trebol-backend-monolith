package org.trebol.api.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.config.CustomProperties;
import org.trebol.api.services.PublicProductsService;

@RestController
@RequestMapping("/public/products")
public class PublicProductsController {

  private final PublicProductsService catalogService;
  private final CustomProperties customProperties;

  @Autowired
  public PublicProductsController(
    PublicProductsService catalogService,
    CustomProperties customProperties
  ) {
    this.catalogService = catalogService;
    this.customProperties = customProperties;
  }

  @GetMapping({"", "/"})
  public Collection<ProductPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    Integer requestPageSize = customProperties.getItemsPerPage();
    return catalogService.readProducts(requestPageSize, 0, allRequestParams);
  }

  @GetMapping({"/{id}", "/{id}/"})
  public ProductPojo readOne(@PathVariable Integer id) {
    return catalogService.getProduct(id);
  }
}
