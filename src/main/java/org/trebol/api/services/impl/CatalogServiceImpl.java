package org.trebol.api.services.impl;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.api.services.PublicProductsService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CatalogServiceImpl
    implements PublicProductsService {

  private final GenericCrudService<ProductCategoryPojo, ProductCategory, Integer> productTypesService;
  private final GenericCrudService<ProductPojo, Product, Integer> productsService;

  @Autowired
  public CatalogServiceImpl(
    GenericCrudService<ProductCategoryPojo, ProductCategory, Integer> productTypesService,
    GenericCrudService<ProductPojo, Product, Integer> productsService) {
    this.productTypesService = productTypesService;
    this.productsService = productsService;
  }

  @Override
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex, Map<String, String> queryParamsMap) {
    Predicate filters = productsService.queryParamsMapToPredicate(queryParamsMap);
    return productsService.read(requestPageSize, requestPageIndex, filters)
        .getItems(); // TODO refactor this
  }

  @Nullable
  @Override
  public ProductPojo getProduct(Integer id) {
    return productsService.find(id);
  }

  @Override
  public Collection<ProductCategoryPojo> getRootCategories() {
    return productTypesService.read(Integer.MAX_VALUE, 0, null)
        .getItems(); // TODO refactor this
  }

  @Override
  public Collection<ProductCategoryPojo> getChildrenCategories(int parentId) {
    Map<String, String> queryParamsMap = Maps.of("productFamily", String.valueOf(parentId)).build();
    Predicate filters = productTypesService.queryParamsMapToPredicate(queryParamsMap);
    return productTypesService.read(Integer.MAX_VALUE, 0, filters)
        .getItems(); // TODO refactor this
  }

}
