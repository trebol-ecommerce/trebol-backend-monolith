package cl.blm.newmarketing.backend.services.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.entities.ProductType;
import cl.blm.newmarketing.backend.model.entities.QProductType;
import cl.blm.newmarketing.backend.model.repositories.ProductTypesRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductTypeCrudServiceImpl
    extends GenericCrudService<ProductType, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductTypeCrudServiceImpl.class);

  @Autowired
  public ProductTypeCrudServiceImpl(ProductTypesRepository productTypes) {
    super(LOG, productTypes);
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QProductType qProductType = QProductType.productType;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qProductType.id.eq(intValue)); // match por id es único
        case "name":
          predicate.and(qProductType.name.likeIgnoreCase("%" + stringValue + "%"));
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
