package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QPerson;

import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.IPeopleJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class PeopleJpaServiceImpl
  extends GenericJpaService<PersonPojo, Person> {

  private static final Logger logger = LoggerFactory.getLogger(PeopleJpaServiceImpl.class);
  private final IPeopleJpaRepository peopleRepository;
  private final ConversionService conversion;

  @Autowired
  public PeopleJpaServiceImpl(IPeopleJpaRepository repository, ConversionService conversion) {
    super(repository);
    this.peopleRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public Optional<Person> getExisting(PersonPojo input) throws BadInputException {
    String idCard = input.getIdNumber();
    if (idCard == null || idCard.isBlank()) {
      throw new BadInputException("Customer does not have ID card");
    } else {
      return peopleRepository.findByIdNumber(idCard);
    }
  }

  @Override
  public PersonPojo convertToPojo(Person source) {
    return conversion.convert(source, PersonPojo.class);
  }

  @Override
  public Person convertToNewEntity(PersonPojo source) {
    return conversion.convert(source, Person.class);
  }

  @Override
  public void applyChangesToExistingEntity(PersonPojo source, Person target) throws BadInputException {
    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    String email = source.getEmail();
    if (email != null && !email.isBlank() && !target.getEmail().equals(email)) {
      target.setEmail(email);
    }

    // phones may be empty, but not null
    String phone1 = source.getPhone1();
    if (phone1 != null) {
      if (!target.getPhone1().equals(phone1)) {
        target.setPhone1(phone1);
      }
    }

    String phone2 = source.getPhone2();
    if (phone2 != null) {
      if (!target.getPhone2().equals(phone2)) {
        target.setPhone2(phone2);
      }
    }
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QPerson qPerson = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qPerson.id.eq(Long.valueOf(stringValue))); // id matching is final
          case "nameLike":
            predicate.and(qPerson.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idNumberLike":
            predicate.and(qPerson.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "emailLike":
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
