package cl.blm.trebol.services.crud.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.trebol.jpa.entities.QPerson;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.jpa.repositories.PeopleRepository;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class PersonCrudServiceImpl
    extends GenericCrudService<PersonPojo, Person, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(PersonCrudServiceImpl.class);

  private final ConversionService conversion;

  @Autowired
  public PersonCrudServiceImpl(PeopleRepository repository, ConversionService conversion) {
    super(repository);
    this.conversion = conversion;
  }

  @Override
  public PersonPojo entity2Pojo(Person source) {
    return conversion.convert(source, PersonPojo.class);
  }

  @Override
  public Person pojo2Entity(PersonPojo source) {
    return conversion.convert(source, Person.class);
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QPerson qPerson = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qPerson.id.eq(intValue)); // id matching is final
          case "name":
            predicate.and(qPerson.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qPerson.idCard.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qPerson.email.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
