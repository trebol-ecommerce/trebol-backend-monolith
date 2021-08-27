package org.trebol.jpa.services;

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
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.QSellStatus;

import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellStatusesJpaCrudServiceImpl
  extends GenericJpaCrudService<SellStatusPojo, SellStatus> {

  private static final Logger logger = LoggerFactory.getLogger(SellStatusesJpaCrudServiceImpl.class);
  private final ConversionService conversion;

  @Autowired
  public SellStatusesJpaCrudServiceImpl(ISellStatusesJpaRepository repository, ConversionService conversion) {
    super(repository);
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
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QSellStatus qSellStatus = QSellStatus.sellStatus;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qSellStatus.id.eq(longValue)); // match por id es único
          case "name":
            predicate.and(qSellStatus.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Override
  public boolean itemExists(SellStatusPojo input) throws BadInputException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
