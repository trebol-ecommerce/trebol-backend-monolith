package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.QProductCategory;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ProductCategoriesPredicateJpaServiceImpl
  implements IPredicateJpaService<ProductCategory> {

  private final Logger logger = LoggerFactory.getLogger(ProductCategoriesPredicateJpaServiceImpl.class);

  @Override
  public QProductCategory getBasePath() {
    return QProductCategory.productCategory;
  }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return getBasePath().id.eq(Long.valueOf(stringValue));
          case "code":
            return getBasePath().code.eq(stringValue);
          case "name":
            predicate.and(getBasePath().name.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(getBasePath().name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "parentCode":
            if (stringValue != null && !stringValue.isEmpty()) {
              predicate.and(getBasePath().parent.code.eq(stringValue));
            }
            break;
          case "parentId":
            if (stringValue == null || stringValue.isEmpty()) {
              predicate.and(getBasePath().parent.isNull());
            } else {
              predicate.and(getBasePath().parent.id.eq(Long.valueOf(stringValue)));
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
}
