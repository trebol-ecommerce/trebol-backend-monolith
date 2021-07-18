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

import org.trebol.api.pojo.SellStatusPojo;
import org.trebol.jpa.entities.QSellStatus;

import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.SellStatusesRepository;
import org.trebol.jpa.services.GenericCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellStatusCrudServiceImpl
    extends GenericCrudService<SellStatusPojo, SellStatus, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellStatusCrudServiceImpl.class);

  private final ConversionService conversion;

  @Autowired
  public SellStatusCrudServiceImpl(SellStatusesRepository sellStatus, ConversionService conversion) {
    super(sellStatus);
    this.conversion = conversion;
  }

  @Nullable
  @Override
  public SellStatusPojo entity2Pojo(SellStatus source) {
    return conversion.convert(source, SellStatusPojo.class);
  }

  @Nullable
  @Override
  public SellStatus pojo2Entity(SellStatusPojo source) {
    return conversion.convert(source, SellStatus.class);
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QSellStatus qSellStatus = QSellStatus.sellStatus;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qSellStatus.id.eq(intValue)); // match por id es único
          case "name":
            predicate.and(qSellStatus.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
