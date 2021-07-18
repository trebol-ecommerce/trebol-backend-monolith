package org.trebol.api.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductFamilyPojo;
import org.trebol.api.pojo.ProductTypePojo;
import org.trebol.api.services.CatalogService;

@RestController
@RequestMapping("/store/categories")
public class StoreCategoriesController {

  private final CatalogService catalogService;

  @Autowired
  public StoreCategoriesController(
    CatalogService catalogService
  ) {
    this.catalogService = catalogService;
  }

  @GetMapping({"", "/"})
  public Collection<ProductFamilyPojo> readProductFamilies() {
    return catalogService.readProductFamilies();
  }

  @GetMapping({"/{familyId}", "/{familyId}/"})
  public Collection<ProductTypePojo> readProductTypesByFamilyId(@PathVariable Integer familyId) {
    if (familyId == null) {
      return catalogService.readProductTypes();
    } else {
      return catalogService.readProductTypesByFamilyId(familyId);
    }
  }
}
