package org.trebol.operation.controllers;

import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.ProductCategory;

import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("/public/categories")
public class PublicCategoriesController {

  private final GenericJpaService<ProductCategoryPojo, ProductCategory> service;

  @Autowired
  public PublicCategoriesController(GenericJpaService<ProductCategoryPojo, ProductCategory> crudService) {
    this.service = crudService;
  }

  @GetMapping({"", "/"})
  public Collection<ProductCategoryPojo> getRootcategories() {
    Map<String, String> parentMatcher = Maps.of("parent", (String)null).build();
    Predicate filters = service.parsePredicate(parentMatcher);
    return service.readMany(Integer.MAX_VALUE, 0, filters).getItems();
  }

  @GetMapping({"/{code}", "/{code}/"})
  public Collection<ProductCategoryPojo> getCategories(@PathVariable Long code) {
    Map<String, String> parentMatcher = (code == null) ?
        Maps.of("parent", (String)null).build() :
        Maps.of("parent", String.valueOf(code)).build();
    Predicate filters = service.parsePredicate(parentMatcher);
    return service.readMany(Integer.MAX_VALUE, 0, filters).getItems();
  }
}
