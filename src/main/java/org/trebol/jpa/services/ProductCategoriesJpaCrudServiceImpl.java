package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

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
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.QProductCategory;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductCategoriesJpaCrudServiceImpl
  extends GenericJpaCrudService<ProductCategoryPojo, ProductCategory> {

  private static final Logger logger = LoggerFactory.getLogger(ProductCategoriesJpaCrudServiceImpl.class);
  private final IProductsCategoriesJpaRepository categoriesRepository;
  private final ConversionService conversion;

  @Autowired
  public ProductCategoriesJpaCrudServiceImpl(IProductsCategoriesJpaRepository repository,
    ConversionService conversion) {
    super(repository);
    this.categoriesRepository = repository;
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
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QProductCategory qProductCategory = QProductCategory.productCategory;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qProductCategory.id.eq(Long.valueOf(stringValue))); // match por id es único
          case "name":
            predicate.and(qProductCategory.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "parent":
            if (stringValue == null) {
              predicate.and(qProductCategory.parent.isNull());
            } else {
              predicate.and(qProductCategory.parent.id.eq(Long.valueOf(stringValue)));
            }
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }

  @Override
  public boolean itemExists(ProductCategoryPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid category name");
    } else {
      return this.categoriesRepository.findByName(name).isPresent();
    }
  }
}
