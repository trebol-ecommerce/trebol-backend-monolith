package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.entities.QProductListItem;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

@Transactional
@Service
public class ProductListItemPredicateJpaServiceImpl
  implements IPredicateJpaService<ProductListItem> {

  private final Logger logger = LoggerFactory.getLogger(ProductListItemPredicateJpaServiceImpl.class);


  @Override
  public QProductListItem getBasePath() { return QProductListItem.productListItem; }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    if (queryParamsMap != null) {
      for (String paramName : queryParamsMap.keySet()) {
        String value = queryParamsMap.get(paramName);
        try {
          switch (paramName) {
            case "listName":
              predicate.and(getBasePath().list.name.eq(value));
              break;
            case "listCode":
              predicate.and(getBasePath().list.code.eq(value));
              break;
            case "productName":
              predicate.and(getBasePath().product.name.eq(value));
              break;
            case "productBarcode":
              predicate.and(getBasePath().product.barcode.eq(value));
              break;
            case "productNameLike":
              predicate.and(getBasePath().product.name.likeIgnoreCase("%" + value + "%"));
              break;
            case "productBarcodeLike":
              predicate.and(getBasePath().product.barcode.likeIgnoreCase("%" + value + "%"));
              break;
            default:
              break;
          }
        } catch (NumberFormatException exc) {
          logger.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, value, exc);
        }
      }
    }
    return predicate;
  }
}
