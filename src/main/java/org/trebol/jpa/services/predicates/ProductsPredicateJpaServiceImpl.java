package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.QProduct;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ProductsPredicateJpaServiceImpl
  implements IPredicateJpaService<Product> {

  private final Logger logger = LoggerFactory.getLogger(ProductsPredicateJpaServiceImpl.class);

  @Override
  public QProduct getBasePath() {
    return QProduct.product;
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
          case "barcode":
            return getBasePath().barcode.eq(stringValue);
          case "name":
            return getBasePath().name.eq(stringValue);
          case "barcodeLike":
            predicate.and(getBasePath().barcode.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "nameLike":
            predicate.and(getBasePath().name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "productCategory":
            predicate.and(getBasePath().productCategory.name.eq(stringValue));
            break;
          case "productCategoryLike":
            predicate.and(getBasePath().productCategory.name.likeIgnoreCase("%" + stringValue + "%"));
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
