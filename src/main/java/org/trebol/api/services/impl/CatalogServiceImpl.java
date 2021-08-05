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
import org.trebol.jpa.services.GenericJpaCrudService;
import org.trebol.api.services.PublicProductsService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CatalogServiceImpl
    implements PublicProductsService {

  private final GenericJpaCrudService<ProductCategoryPojo, ProductCategory> productTypesService;
  private final GenericJpaCrudService<ProductPojo, Product> productsService;

  @Autowired
  public CatalogServiceImpl(
    GenericJpaCrudService<ProductCategoryPojo, ProductCategory> productTypesService,
    GenericJpaCrudService<ProductPojo, Product> productsService) {
    this.productTypesService = productTypesService;
    this.productsService = productsService;
  }

  @Override
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex, Map<String, String> queryParamsMap) {
    Predicate filters = productsService.queryParamsMapToPredicate(queryParamsMap);
    return productsService.readMany(requestPageSize, requestPageIndex, filters)
        .getItems(); // TODO refactor this
  }

  @Nullable
  @Override
  public ProductPojo getProduct(long id) {
    return productsService.readOne(id);
  }

  @Override
  public Collection<ProductCategoryPojo> getRootCategories() {
    return productTypesService.readMany(Integer.MAX_VALUE, 0, null)
        .getItems(); // TODO refactor this
  }

  @Override
  public Collection<ProductCategoryPojo> getChildrenCategories(long parentId) {
    Map<String, String> queryParamsMap = Maps.of("productFamily", String.valueOf(parentId)).build();
    Predicate filters = productTypesService.queryParamsMapToPredicate(queryParamsMap);
    return productTypesService.readMany(Integer.MAX_VALUE, 0, filters)
        .getItems(); // TODO refactor this
  }

}
