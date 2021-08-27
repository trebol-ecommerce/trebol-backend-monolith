package org.trebol.api.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.api.IProductCategoriesService;

@RestController
@RequestMapping("/public/categories")
public class PublicCategoriesController {

  private final IProductCategoriesService service;

  @Autowired
  public PublicCategoriesController(IProductCategoriesService productCategoriesService) {
    this.service = productCategoriesService;
  }

  @GetMapping({"", "/"})
  public Collection<ProductCategoryPojo> getRootcategories() {
    return service.getRootCategories();
  }

  @GetMapping({"/{parentId}", "/{parentId}/"})
  public Collection<ProductCategoryPojo> getCategories(@PathVariable Integer parentId) {
    if (parentId == null) {
      return service.getRootCategories();
    } else {
      return service.getChildrenCategories(parentId);
    }
  }
}
