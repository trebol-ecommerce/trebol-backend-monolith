package cl.blm.newmarketing.backend.services.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.entities.QSellType;
import cl.blm.newmarketing.backend.model.entities.SellType;
import cl.blm.newmarketing.backend.model.repositories.SellTypesRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellTypeCrudServiceImpl
    extends GenericCrudService<SellType, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellTypeCrudServiceImpl.class);

  @Autowired
  public SellTypeCrudServiceImpl(SellTypesRepository sellTypes) {
    super(LOG, sellTypes);
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSellType qSellType = QSellType.sellType;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qSellType.id.eq(intValue)); // match por id es único
        case "name":
          predicate.and(qSellType.name.likeIgnoreCase("%" + stringValue + "%"));
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
