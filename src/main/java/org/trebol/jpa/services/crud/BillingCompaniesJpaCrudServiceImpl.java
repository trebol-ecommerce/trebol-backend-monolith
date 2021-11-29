package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingCompanyPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class BillingCompaniesJpaCrudServiceImpl
  extends GenericCrudJpaService<BillingCompanyPojo, BillingCompany> {

  private final IBillingCompaniesJpaRepository billingTypesRepository;

  @Autowired
  public BillingCompaniesJpaCrudServiceImpl(IBillingCompaniesJpaRepository repository,
                                            ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(BillingCompaniesJpaCrudServiceImpl.class));
    this.billingTypesRepository = repository;
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
}
