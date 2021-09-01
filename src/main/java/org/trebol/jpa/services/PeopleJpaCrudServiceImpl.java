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

import org.trebol.jpa.entities.QPerson;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IPeopleJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class PeopleJpaCrudServiceImpl
  extends GenericJpaCrudService<PersonPojo, Person> {

  private static final Logger logger = LoggerFactory.getLogger(PeopleJpaCrudServiceImpl.class);
  private final IPeopleJpaRepository peopleRepository;
  private final ConversionService conversion;

  @Autowired
  public PeopleJpaCrudServiceImpl(IPeopleJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.peopleRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public boolean itemExists(PersonPojo input) throws BadInputException {
    String idCard = input.getIdNumber();
    if (idCard == null || idCard.isBlank()) {
      throw new BadInputException("Customer does not have ID card");
    } else {
      return (peopleRepository.findByIdNumber(idCard).isPresent());
    }
  }

  @Nullable
  @Override
  public PersonPojo entity2Pojo(Person source) {
    return conversion.convert(source, PersonPojo.class);
  }

  @Nullable
  @Override
  public Person pojo2Entity(PersonPojo source) {
    return conversion.convert(source, Person.class);
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QPerson qPerson = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qPerson.id.eq(longValue)); // id matching is final
          case "name":
            predicate.and(qPerson.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qPerson.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qPerson.email.likeIgnoreCase("%" + stringValue + "%"));
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
