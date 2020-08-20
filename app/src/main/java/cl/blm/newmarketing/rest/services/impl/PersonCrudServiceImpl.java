package cl.blm.newmarketing.rest.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.rest.dtos.PersonDto;
import cl.blm.newmarketing.model.entities.Person;
import cl.blm.newmarketing.model.entities.Phone;
import cl.blm.examples.spring.rest.model.entities.QPerson;
import cl.blm.newmarketing.model.repositories.PersonRepository;
import cl.blm.newmarketing.model.repositories.PhoneRepository;
import cl.blm.newmarketing.rest.services.CrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class PersonCrudServiceImpl
    implements CrudService<PersonDto, Long> {
  private static final Logger LOG = LoggerFactory.getLogger(PersonCrudServiceImpl.class);

  @Autowired PersonRepository people;
  @Autowired PhoneRepository phones;
  @Autowired ConversionService conversion;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QPerson qPerson = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String paramValue = queryParamsMap.get(paramName);
      try {
        Long parsedValueL;
        switch (paramName) {
        case "id":
          parsedValueL = Long.valueOf(paramValue);
          return predicate.and(qPerson.id.eq(parsedValueL)); // match por id es único
        case "name":
          predicate.and(qPerson.fullName.likeIgnoreCase("%" + paramValue + "%"));
          break;
        case "idnumber":
          predicate.and(qPerson.idNumber.likeIgnoreCase("%" + paramValue + "%"));
          break;
        case "email":
          predicate.and(qPerson.email.likeIgnoreCase("%" + paramValue + "%"));
          break;
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.error("Param '{}' couldn't be parsed as number (value: '{}')", paramName, paramValue, exc);
      }
    }

    return predicate;
  }

  @Nullable
  @Override
  public PersonDto create(PersonDto dto) {
    LOG.debug("create({})", dto);
    Person newEntity = conversion.convert(dto, Person.class);
    if (dto.getPersonId() != null && people.findById(dto.getPersonId()).isPresent()) {
      return null;
    } else {
      newEntity = people.saveAndFlush(newEntity);
  
      Collection<Long> phoneNumbers = dto.getPersonPhones();
      if (phoneNumbers != null) {
        for (Long number : phoneNumbers) {
          Phone phone = new Phone(number);
          phone.setPerson(newEntity);
          phones.saveAndFlush(phone);
        }
      }
  
      PersonDto newDto = conversion.convert(newEntity, PersonDto.class);
      newDto.setPersonPhones(phoneNumbers);
      return newDto;
    }
  }

  @Override
  public Collection<PersonDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);

    Iterable<Person> iteratePeople;
    if (filters == null) {
      iteratePeople = people.findAll(paged);
    } else {
      iteratePeople = people.findAll(filters, paged);
    }

    List<PersonDto> pagina = new ArrayList<>();
    for (Person person : iteratePeople) {
      PersonDto dto = conversion.convert(person, PersonDto.class);
      pagina.add(dto);
    }

    return pagina;
  }

  @Nullable
  @Override
  public PersonDto update(PersonDto dto) {
    LOG.debug("update({})", dto);
    Optional<Person> existing = people.findById(dto.getPersonId());
    if (!existing.isPresent()) {
      return null;
    } else {
      Person existingPerson = existing.get();
      Person newPerson = conversion.convert(dto, Person.class);
      if (newPerson.equals(existingPerson)) {
        return dto;
      } else {
        try {
          newPerson = people.saveAndFlush(newPerson);
          return conversion.convert(newPerson, PersonDto.class);
        } catch (Exception exc) {
          LOG.error("Person could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(Long id) {
    LOG.debug("delete({})", id);
    try {
      people.deleteById(id);
      people.flush();
      return !people.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete person with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public PersonDto find(Long id) {
    LOG.debug("find({})", id);
    Optional<Person> personById = people.findById(id);
    if (personById.isPresent()) {
      return null;
    } else {
      return conversion.convert(personById.get(), PersonDto.class);
    }
  }
}
