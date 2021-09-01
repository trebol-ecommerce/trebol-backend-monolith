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

import org.trebol.jpa.entities.QCustomer;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.ICustomersJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class CustomersJpaCrudServiceImpl
  extends GenericJpaCrudService<CustomerPojo, Customer> {

  private static final Logger logger = LoggerFactory.getLogger(CustomersJpaCrudServiceImpl.class);
  private final ICustomersJpaRepository customersRepository;
  private final ConversionService conversion;

  @Autowired
  public CustomersJpaCrudServiceImpl(ICustomersJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.customersRepository = repository;
    this.conversion = conversion;
  }

  @Nullable
  @Override
  public CustomerPojo entity2Pojo(Customer source) {
    CustomerPojo target = conversion.convert(source, CustomerPojo.class);
    if (target != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        target.setPerson(person);
      }
    }
    return target;
  }

  @Nullable
  @Override
  public Customer pojo2Entity(CustomerPojo source) {
    Customer target = conversion.convert(source, Customer.class);
    if (target != null) {
      Person personTarget = conversion.convert(source.getPerson(), Person.class);
      if (personTarget != null) {
        target.setPerson(personTarget);
      }
    }
    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QCustomer qCustomer = QCustomer.customer;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qCustomer.id.eq(longValue)); // id matching is final
          case "name":
            predicate.and(qCustomer.person.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qCustomer.person.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qCustomer.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
  public boolean itemExists(CustomerPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Customer does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null || idNumber.isBlank()) {
        throw new BadInputException("Customer does not have an ID card");
      } else {
        return (customersRepository.findByPersonIdNumber(idNumber).isPresent());
      }
    }
  }
}
