package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingTypePojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class BillingTypesJpaCrudServiceImpl
  extends GenericCrudJpaService<BillingTypePojo, BillingType> {

  private final IBillingTypesJpaRepository billingTypesRepository;

  @Autowired
  public BillingTypesJpaCrudServiceImpl(IBillingTypesJpaRepository repository,
                                        ITwoWayConverterJpaService<BillingTypePojo, BillingType> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(BillingTypesJpaCrudServiceImpl.class));
    this.billingTypesRepository = repository;
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
}
