package org.trebol.api.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.api.services.PublicProductsService;

@RestController
@RequestMapping("/public/categories")
public class PublicCategoriesController {

  private final PublicProductsService catalogService;

  @Autowired
  public PublicCategoriesController(
    PublicProductsService catalogService
  ) {
    this.catalogService = catalogService;
  }

  @GetMapping({"", "/"})
  public Collection<ProductCategoryPojo> getRootcategories() {
    return catalogService.getRootCategories();
  }

  @GetMapping({"/{parentId}", "/{parentId}/"})
  public Collection<ProductCategoryPojo> getCategories(@PathVariable Integer parentId) {
    if (parentId == null) {
      return catalogService.getRootCategories();
    } else {
      return catalogService.getChildrenCategories(parentId);
    }
  }
}
