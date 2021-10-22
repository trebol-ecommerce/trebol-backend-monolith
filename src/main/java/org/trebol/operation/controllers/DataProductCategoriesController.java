package org.trebol.operation.controllers;

import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.operation.GenericDataController;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.GenericJpaService;

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
    OperationProperties globals,
    GenericJpaService<ProductCategoryPojo, ProductCategory> crudService) {
    super(globals, crudService);
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductCategoryPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
    return super.readMany(null, null, allRequestParams);
  }

  @GetMapping({"/{parentId}", "/{parentId}/"})
  public DataPagePojo<ProductCategoryPojo> readChildren(@PathVariable Long parentId) {
    Map<String, String> queryParamsMap = Maps.of("parentId", String.valueOf(parentId)).build();
    return super.readMany(null, null, queryParamsMap);
  }
}
