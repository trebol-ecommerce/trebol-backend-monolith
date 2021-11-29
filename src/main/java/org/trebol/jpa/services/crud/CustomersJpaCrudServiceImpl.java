package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class CustomersJpaCrudServiceImpl
  extends GenericCrudJpaService<CustomerPojo, Customer> {

  private final ICustomersJpaRepository customersRepository;

  @Autowired
  public CustomersJpaCrudServiceImpl(ICustomersJpaRepository repository,
                                     ITwoWayConverterJpaService<CustomerPojo, Customer> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(CustomersJpaCrudServiceImpl.class));
    this.customersRepository = repository;
  }

  @Override
  public Optional<Customer> getExisting(CustomerPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Customer does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null || idNumber.isBlank()) {
        throw new BadInputException("Customer does not have an ID card");
      } else {
        return customersRepository.findByPersonIdNumber(idNumber);
      }
    }
  }
}
