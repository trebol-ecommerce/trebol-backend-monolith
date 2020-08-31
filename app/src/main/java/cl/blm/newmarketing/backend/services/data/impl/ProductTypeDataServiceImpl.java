package cl.blm.newmarketing.backend.services.data.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.api.pojo.ProductTypePojo;
import cl.blm.newmarketing.backend.model.entities.ProductType;
import cl.blm.newmarketing.backend.model.entities.QProductType;
import cl.blm.newmarketing.backend.model.repositories.ProductTypesRepository;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductTypeDataServiceImpl
    extends GenericDataService<ProductTypePojo, ProductType, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductTypeDataServiceImpl.class);

  private ConversionService conversion;

  @Autowired
  public ProductTypeDataServiceImpl(ProductTypesRepository productTypes, ConversionService conversion) {
    super(LOG, productTypes);
    this.conversion = conversion;
  }

  @Override
  public ProductTypePojo entity2Pojo(ProductType source) {
    return conversion.convert(source, ProductTypePojo.class);
  }

  @Override
  public ProductType pojo2Entity(ProductTypePojo source) {
    return conversion.convert(source, ProductType.class);
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
