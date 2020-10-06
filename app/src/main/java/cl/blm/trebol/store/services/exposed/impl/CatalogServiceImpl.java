package cl.blm.trebol.store.services.exposed.impl;

import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import cl.blm.trebol.store.api.pojo.ProductFamilyPojo;
import cl.blm.trebol.store.api.pojo.ProductPojo;
import cl.blm.trebol.store.api.pojo.ProductTypePojo;
import cl.blm.trebol.store.jpa.entities.Product;
import cl.blm.trebol.store.jpa.entities.ProductFamily;
import cl.blm.trebol.store.jpa.entities.ProductType;
import cl.blm.trebol.store.services.crud.GenericCrudService;
import cl.blm.trebol.store.services.exposed.CatalogService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CatalogServiceImpl
    implements CatalogService {

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
