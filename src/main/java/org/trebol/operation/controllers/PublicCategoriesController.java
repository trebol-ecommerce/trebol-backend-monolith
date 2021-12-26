package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/public/categories")
public class PublicCategoriesController {

  private final GenericCrudJpaService<ProductCategoryPojo, ProductCategory> service;
  private final IPredicateJpaService<ProductCategory> predicateService;

  @Autowired
  public PublicCategoriesController(GenericCrudJpaService<ProductCategoryPojo, ProductCategory> crudService,
                                    IPredicateJpaService<ProductCategory> predicateService) {
    this.service = crudService;
    this.predicateService = predicateService;
  }
  
  /**
   * @deprecated
   * This method is no longer acceptable to get the product categories.
   */
  @Deprecated(forRemoval = true)
  @GetMapping({"", "/"})
  public Collection<ProductCategoryPojo> getRootcategories() {
    DataPagePojo<ProductCategoryPojo> page = service.readMany(0, Integer.MAX_VALUE, null, whereNoParentExists());
    return page.getItems();
  }

  /**
   * @deprecated
   * This method is no longer acceptable to get the product categories by id.
   */
  @Deprecated(forRemoval = true)
  @GetMapping({"/{code}", "/{code}/"})
  public Collection<ProductCategoryPojo> getCategories(@PathVariable String code) {
    DataPagePojo<ProductCategoryPojo> page = service.readMany(0, Integer.MAX_VALUE, null, whereParentCodeIs(code));
    return page.getItems();
  }

  private Predicate whereNoParentExists() {
    Map<String, String> parentMatcher = Maps.of("parentId", (String)null).build();
    return predicateService.parseMap(parentMatcher);
  }

  private Predicate whereParentCodeIs(String code) {
    Map<String, String> parentMatcher = Maps.of("parentCode", code).build();
    return predicateService.parseMap(parentMatcher);
  }
}
