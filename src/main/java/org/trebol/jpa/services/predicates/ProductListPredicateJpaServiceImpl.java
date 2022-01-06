package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.entities.QProductList;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

@Transactional
@Service
public class ProductListPredicateJpaServiceImpl
  implements IPredicateJpaService<ProductList> {

  private final Logger logger = LoggerFactory.getLogger(ProductListPredicateJpaServiceImpl.class);


  @Override
  public EntityPathBase<ProductList> getBasePath() { return QProductList.productList; }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    if (queryParamsMap != null) {
      QProductList qProductList = QProductList.productList;
      for (String paramName : queryParamsMap.keySet()) {
        String value = queryParamsMap.get(paramName);
        try {
          switch (paramName) {
            case "name":
              return predicate.and(qProductList.name.eq(value));
            case "code":
              return predicate.and(qProductList.code.eq(value));
            case "nameLike":
              predicate.and(qProductList.name.likeIgnoreCase("%" + value + "%"));
              break;
            case "codeLike":
              predicate.and(qProductList.code.likeIgnoreCase("%" + value + "%"));
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
