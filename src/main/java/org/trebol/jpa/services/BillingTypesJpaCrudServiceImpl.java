package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QBillingType;

import org.trebol.api.pojo.BillingTypePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.repositories.IPeopleJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class BillingTypesJpaCrudServiceImpl
  extends GenericJpaCrudService<BillingTypePojo, BillingType> {

  private static final Logger logger = LoggerFactory.getLogger(BillingTypesJpaCrudServiceImpl.class);
  private final IBillingTypesJpaRepository billingTypesRepository;
  private final ConversionService conversion;

  @Autowired
  public BillingTypesJpaCrudServiceImpl(IBillingTypesJpaRepository repository, ConversionService conversion) {
    super(repository);
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
    return conversion.convert(source, BillingType.class);
  }

  @Override
  public void applyChangesToExistingEntity(BillingTypePojo source, BillingType target) throws BadInputException {
    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }
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
