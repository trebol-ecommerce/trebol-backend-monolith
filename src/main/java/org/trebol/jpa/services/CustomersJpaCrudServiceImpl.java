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
  private final GenericJpaCrudService<PersonPojo, Person> peopleService;
  private final ICustomersJpaRepository customersRepository;
  private final ConversionService conversion;

  @Autowired
  public CustomersJpaCrudServiceImpl(ICustomersJpaRepository repository,
    GenericJpaCrudService<PersonPojo, Person> peopleService, ConversionService conversion) {
    super(repository);
    this.peopleService = peopleService;
    this.customersRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public CustomerPojo convertToPojo(Customer source) {
    CustomerPojo target = new CustomerPojo();
    target.setId(source.getId());
    PersonPojo targetPerson = peopleService.convertToPojo(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Customer convertToNewEntity(CustomerPojo source) throws BadInputException {
    Customer target = new Customer();
    Person targetPerson = peopleService.convertToNewEntity(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public void applyChangesToExistingEntity(CustomerPojo source, Customer target) throws BadInputException {
    Person targetPerson = target.getPerson();
    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson == null) {
      throw new BadInputException("Customer must have a person profile");
    }
    peopleService.applyChangesToExistingEntity(sourcePerson, targetPerson);
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
          case "nameLike":
            predicate.and(qCustomer.person.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumberLike":
            predicate.and(qCustomer.person.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "emailLike":
            predicate.and(qCustomer.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
