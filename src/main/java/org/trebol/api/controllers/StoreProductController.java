package org.trebol.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.services.CatalogService;

@RestController
@RequestMapping("/store/product")
public class StoreProductController {

  private final CatalogService catalogService;

  @Autowired
  public StoreProductController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @GetMapping({"/{id}", "/{id}/"})
  public ProductPojo readOne(@PathVariable Integer id) {
    return catalogService.readProduct(id);
  }
}
