package org.trebol.jpa.services.impl;

import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.QProductCategory;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.repositories.ProductsCategoriesRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductCategoryCrudServiceImpl
    extends GenericCrudService<ProductCategoryPojo, ProductCategory, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryCrudServiceImpl.class);

  private final ProductsCategoriesRepository repository;
  private final ConversionService conversion;

  @Autowired
  public ProductCategoryCrudServiceImpl(ProductsCategoriesRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Nullable
  @Override
  public ProductCategoryPojo entity2Pojo(ProductCategory source) {
    return conversion.convert(source, ProductCategoryPojo.class);
  }

  @Nullable
  @Override
  public ProductCategory pojo2Entity(ProductCategoryPojo source) {
    return conversion.convert(source, ProductCategory.class);
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QProductCategory qProductCategory = QProductCategory.productCategory;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qProductCategory.id.eq(intValue)); // match por id es único
          case "name":
            predicate.and(qProductCategory.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "parent":
            intValue = Integer.valueOf(stringValue);
            predicate.and(qProductCategory.parent.id.eq(intValue));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        LOG.error("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
