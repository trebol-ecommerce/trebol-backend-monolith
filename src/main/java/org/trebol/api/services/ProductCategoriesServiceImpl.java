package org.trebol.api.services;

import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.lang.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.api.IProductCategoriesService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class ProductCategoriesServiceImpl
    implements IProductCategoriesService {

  private final GenericJpaCrudService<ProductCategoryPojo, ProductCategory> crudService;

  @Autowired
  public ProductCategoriesServiceImpl(GenericJpaCrudService<ProductCategoryPojo, ProductCategory> crudService) {
    this.crudService = crudService;
  }

  @Override
  public Collection<ProductCategoryPojo> getRootCategories() {
    return crudService.readMany(Integer.MAX_VALUE, 0, null)
        .getItems(); // TODO refactor this
  }

  @Override
  public Collection<ProductCategoryPojo> getChildrenCategories(long parentId) {
    Map<String, String> queryParamsMap = Maps.of("parentId", String.valueOf(parentId)).build();
    Predicate filters = crudService.parsePredicate(queryParamsMap);
    return crudService.readMany(Integer.MAX_VALUE, 0, filters)
        .getItems(); // TODO refactor this
  }

}
