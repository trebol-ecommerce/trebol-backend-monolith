package org.trebol.jpa.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.pojo.BillingCompanyPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.QBillingCompany;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class BillingCompaniesJpaServiceImpl
  extends GenericJpaService<BillingCompanyPojo, BillingCompany> {

  private final IBillingCompaniesJpaRepository billingTypesRepository;
  private final ConversionService conversion;

  @Autowired
  public BillingCompaniesJpaServiceImpl(IBillingCompaniesJpaRepository repository, ConversionService conversion) {
    super(repository, LoggerFactory.getLogger(BillingCompaniesJpaServiceImpl.class));
    this.billingTypesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public Optional<BillingCompany> getExisting(BillingCompanyPojo input) throws BadInputException {
    String idNumber = input.getIdNumber();
    if (idNumber == null || idNumber.isBlank()) {
      throw new BadInputException("Billing company has no id number");
    } else {
      return billingTypesRepository.findByIdNumber(idNumber);
    }
  }

  @Override
  public BillingCompanyPojo convertToPojo(BillingCompany source) {
    return conversion.convert(source, BillingCompanyPojo.class);
  }

  @Override
  public BillingCompany convertToNewEntity(BillingCompanyPojo source) {
    BillingCompany target = new BillingCompany();
    target.setIdNumber(source.getIdNumber());
    target.setName(source.getName());
    return target;
  }

  @Override
  public BillingCompany applyChangesToExistingEntity(BillingCompanyPojo source, BillingCompany existing)
          throws BadInputException {
    BillingCompany target = new BillingCompany(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QBillingCompany qBillingCompany = QBillingCompany.billingCompany;
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return qBillingCompany.id.eq(Long.valueOf(stringValue));
          case "idNumber":
            return qBillingCompany.idNumber.eq(stringValue);
          case "name":
            predicate.and(qBillingCompany.name.eq(stringValue));
            break;
          case "idNumberLike":
            predicate.and(qBillingCompany.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "nameLike":
            predicate.and(qBillingCompany.name.likeIgnoreCase("%" + stringValue + "%"));
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
