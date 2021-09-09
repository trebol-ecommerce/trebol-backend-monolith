package org.trebol.jpa.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QSalesperson;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SalespeopleJpaCrudServiceImpl
  extends GenericJpaCrudService<SalespersonPojo, Salesperson> {

  private static final Logger logger = LoggerFactory.getLogger(SalespeopleJpaCrudServiceImpl.class);
  private final ISalespeopleJpaRepository salespeopleRepository;
  private final GenericJpaCrudService<PersonPojo, Person> peopleService;
  private final ConversionService conversion;

  @Autowired
  public SalespeopleJpaCrudServiceImpl(ISalespeopleJpaRepository repository,
    GenericJpaCrudService<PersonPojo, Person> peopleService, ConversionService conversion) {
    super(repository);
    this.salespeopleRepository = repository;
    this.peopleService = peopleService;
    this.conversion = conversion;
  }

  @Override
  public SalespersonPojo convertToPojo(Salesperson source) {
    SalespersonPojo target = conversion.convert(source, SalespersonPojo.class);
    if (target != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        target.setPerson(person);
      }
    }
    return target;
  }

  @Override
  public Salesperson convertToNewEntity(SalespersonPojo source) {
    Salesperson target = conversion.convert(source, Salesperson.class);
    if (target != null) {
      Person personTarget = conversion.convert(source.getPerson(), Person.class);
      if (personTarget != null) {
        target.setPerson(personTarget);
      }
    }
    return target;
  }

  @Override
  public void applyChangesToExistingEntity(SalespersonPojo source, Salesperson target) throws BadInputException {
    Person targetPerson = target.getPerson();
    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson == null) {
      throw new BadInputException("Customer must have a person profile");
    }
    peopleService.applyChangesToExistingEntity(sourcePerson, targetPerson);
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    logger.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSalesperson qSalesperson = QSalesperson.salesperson;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qSalesperson.id.eq(longValue)); // id matching is final
          case "name":
            predicate.and(qSalesperson.person.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qSalesperson.person.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qSalesperson.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
  public boolean itemExists(SalespersonPojo input) throws BadInputException {
    PersonPojo person = input.getPerson();
    if (person == null) {
      throw new BadInputException("Salesperson does not have profile information");
    } else {
      String idNumber = person.getIdNumber();
      if (idNumber == null) {
        throw new BadInputException("Salesperson does not have an ID card");
      } else {
        return (salespeopleRepository.findByPersonIdNumber(idNumber).isPresent());
      }
    }
  }
}
