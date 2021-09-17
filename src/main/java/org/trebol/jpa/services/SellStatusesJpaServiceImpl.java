package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.pojo.SellStatusPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.QSellStatus;

import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellStatusesJpaServiceImpl
  extends GenericJpaService<SellStatusPojo, SellStatus> {

  private static final Logger logger = LoggerFactory.getLogger(SellStatusesJpaServiceImpl.class);
  private final ISellStatusesJpaRepository statusesRepository;
  private final ConversionService conversion;

  @Autowired
  public SellStatusesJpaServiceImpl(ISellStatusesJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.statusesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public SellStatusPojo convertToPojo(SellStatus source) {
    return conversion.convert(source, SellStatusPojo.class);
  }

  @Override
  public SellStatus convertToNewEntity(SellStatusPojo source) {
    SellStatus target = new SellStatus();
    target.setCode(source.getCode());
    target.setName(source.getName());
    return target;
  }

  @Override
  public void applyChangesToExistingEntity(SellStatusPojo source, SellStatus target) throws BadInputException {
    Integer code = source.getCode();
    if (code != null && !target.getCode().equals(code))  {
      target.setCode(code);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }
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
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }

  @Override
  public Optional<SellStatus> getExisting(SellStatusPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid status name");
    } else {
      return statusesRepository.findByName(name);
    }
  }
}
