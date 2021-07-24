package org.trebol.api.services.impl;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.ProductFamilyPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.ProductTypePojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductFamily;
import org.trebol.jpa.entities.ProductType;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.api.services.PublicProductsService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CatalogServiceImpl
    implements PublicProductsService {

  private final GenericCrudService<ProductFamilyPojo, ProductFamily, Integer> productFamiliesService;
  private final GenericCrudService<ProductTypePojo, ProductType, Integer> productTypesService;
  private final GenericCrudService<ProductPojo, Product, Integer> productsService;

  @Autowired
  public CatalogServiceImpl(GenericCrudService<ProductFamilyPojo, ProductFamily, Integer> productFamiliesService,
      GenericCrudService<ProductTypePojo, ProductType, Integer> productTypesService,
      GenericCrudService<ProductPojo, Product, Integer> productsService) {
    this.productFamiliesService = productFamiliesService;
    this.productTypesService = productTypesService;
    this.productsService = productsService;
  }

  @Override
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex, Map<String, String> queryParamsMap) {
    Predicate filters = productsService.queryParamsMapToPredicate(queryParamsMap);
    return productsService.read(requestPageSize, requestPageIndex, filters);
  }

  @Nullable
  @Override
  public ProductPojo readProduct(Integer id) {
    return productsService.find(id);
  }

  @Override
  public Collection<ProductTypePojo> readProductTypes() {
    return productTypesService.read(Integer.MAX_VALUE, 0, null);
  }

  @Override
  public Collection<ProductTypePojo> readProductTypesByFamilyId(int productFamilyId) {
    Map<String, String> queryParamsMap = Maps.of("productFamily", String.valueOf(productFamilyId)).build();
    Predicate filters = productTypesService.queryParamsMapToPredicate(queryParamsMap);
    return productTypesService.read(Integer.MAX_VALUE, 0, filters);
  }

  @Override
  public Collection<ProductFamilyPojo> readProductFamilies() {
    return productFamiliesService.read(Integer.MAX_VALUE, 0, null);
  }

}
