package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QBillingType;

import org.trebol.pojo.BillingTypePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class BillingTypesJpaServiceImpl
  extends GenericJpaService<BillingTypePojo, BillingType> {

  private final IBillingTypesJpaRepository billingTypesRepository;
  private final ConversionService conversion;

  @Autowired
  public BillingTypesJpaServiceImpl(IBillingTypesJpaRepository repository, ConversionService conversion) {
    super(repository, LoggerFactory.getLogger(BillingTypesJpaServiceImpl.class));
    this.billingTypesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public Optional<BillingType> getExisting(BillingTypePojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Billing type has no name");
    } else {
      return billingTypesRepository.findByName(name);
    }
  }

  @Override
  public BillingTypePojo convertToPojo(BillingType source) {
    return conversion.convert(source, BillingTypePojo.class);
  }

  @Override
  public BillingType convertToNewEntity(BillingTypePojo source) {
    BillingType target = new BillingType();
    target.setName(source.getName());
    return target;
  }

  @Override
  public BillingType applyChangesToExistingEntity(BillingTypePojo source, BillingType existing) throws BadInputException {
    BillingType target = new BillingType(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QBillingType qBillingType = QBillingType.billingType;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qBillingType.id.eq(Long.valueOf(stringValue))); // id matching is final
          case "name":
            predicate.and(qBillingType.name.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(qBillingType.name.likeIgnoreCase("%" + stringValue + "%"));
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
