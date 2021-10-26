package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QCustomer;

import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.ICustomersJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class CustomersJpaServiceImpl
  extends GenericJpaService<CustomerPojo, Customer> {

  private final GenericJpaService<PersonPojo, Person> peopleService;
  private final ICustomersJpaRepository customersRepository;

  @Autowired
  public CustomersJpaServiceImpl(ICustomersJpaRepository repository,
                                 GenericJpaService<PersonPojo, Person> peopleService) {
    super(repository, LoggerFactory.getLogger(CustomersJpaServiceImpl.class));
    this.peopleService = peopleService;
    this.customersRepository = repository;
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
  public Customer applyChangesToExistingEntity(CustomerPojo source, Customer existing) throws BadInputException {
    Customer target = new Customer(existing);
    Person existingPerson = existing.getPerson();

    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson == null) {
      throw new BadInputException("Customer must have a person profile");
    }
    Person person = peopleService.applyChangesToExistingEntity(sourcePerson, existingPerson);
    target.setPerson(person);

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QCustomer qCustomer = QCustomer.customer;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qCustomer.id.eq(Long.valueOf(stringValue))); // id matching is final
          case "name":
            predicate.and(qCustomer.person.firstName.eq(stringValue)
                    .or(qCustomer.person.lastName.eq(stringValue)));
            break;
          case "firstName":
            predicate.and(qCustomer.person.firstName.eq(stringValue));
            break;
          case "lastName":
            predicate.and(qCustomer.person.lastName.eq(stringValue));
            break;
          case "idNumber":
            predicate.and(qCustomer.person.idNumber.eq(stringValue));
            break;
          case "email":
            predicate.and(qCustomer.person.email.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(qCustomer.person.firstName.likeIgnoreCase("%" + stringValue + "%")
                    .or(qCustomer.person.lastName.likeIgnoreCase("%" + stringValue + "%")));
            break;
          case "firstNameLike":
            predicate.and(qCustomer.person.firstName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "lastNameLike":
            predicate.and(qCustomer.person.lastName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idNumberLike":
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
