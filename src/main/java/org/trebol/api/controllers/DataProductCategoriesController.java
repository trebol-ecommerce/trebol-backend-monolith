package org.trebol.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.GenericDataController;
import org.trebol.api.DataPage;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.config.CustomProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.GenericJpaCrudService;

/**
 * API point of entry for ProductCategory entities
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/data/product_categories")
public class DataProductCategoriesController
  extends GenericDataController<ProductCategoryPojo, ProductCategory> {

  @Autowired
  public DataProductCategoriesController(
    CustomProperties globals,
    GenericJpaCrudService<ProductCategoryPojo, ProductCategory> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_categories:read')")
  public DataPage<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping({"/{code}", "/{code}/"})
  @PreAuthorize("hasAuthority('product_categories:read')")
  public ProductCategoryPojo readOne(@PathVariable String code) {
    throw new UnsupportedOperationException("Method not implemented");
  }
}
