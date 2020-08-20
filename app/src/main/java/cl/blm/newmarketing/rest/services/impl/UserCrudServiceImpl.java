package cl.blm.newmarketing.rest.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
import cl.blm.newmarketing.rest.dtos.UserDto;
import cl.blm.newmarketing.model.entities.Person;
import cl.blm.examples.spring.rest.model.entities.QUser;
import cl.blm.newmarketing.model.entities.User;
import cl.blm.newmarketing.model.repositories.PersonRepository;
import cl.blm.newmarketing.model.repositories.UserRepository;
import cl.blm.newmarketing.rest.services.CrudService;
import cl.blm.newmarketing.rest.services.UtilityService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserCrudServiceImpl
    implements CrudService<UserDto, Long> {
  private static final Logger LOG = LoggerFactory.getLogger(UserCrudServiceImpl.class);

  @Autowired private UserRepository users;
  @Autowired private PersonRepository people;
  @Autowired private ConversionService conversion;
  @Autowired private UtilityService util;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QUser qUser = QUser.user;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String paramValue = queryParamsMap.get(paramName);
      try {
        Long parsedValueL;
        switch (paramName) {
        case "id":
          parsedValueL = Long.valueOf(paramValue);
          predicate.and(qUser.id.eq(parsedValueL));
          return predicate;
        case "name":
          predicate.and(qUser.name.likeIgnoreCase("%" + paramValue + "%"));
          break;
        case "registration":
          Date date = util.formatString(paramValue.trim());
          if (date != null) {
            predicate.and(qUser.registrationDate.eq(date));
          }
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
  public UserDto create(UserDto dto) {
    LOG.debug("create({})", dto);
    PersonDto dtoPerson = dto.getPerson();
    if (dtoPerson == null || people.existsById(dtoPerson.getPersonId())) {
      return null;
    } else {
      User user = conversion.convert(dto, User.class);
      if (dtoPerson.getPersonId() == null) {
        Person person = conversion.convert(dtoPerson, Person.class);
        user.setPerson(person);
      } else {
        Optional<Person> personQuery = people.findById(dto.getPerson().getPersonId());
        if (!personQuery.isPresent()) {
          throw new EntityNotFoundException("Associated person could not be found");
        } else {
          user.setPerson(personQuery.get());
        }
      }
      user = users.saveAndFlush(user);
      dto = conversion.convert(user, UserDto.class);
      dtoPerson = conversion.convert(user.getPerson(), PersonDto.class);
      dto.setPerson(dtoPerson);

      return dto;
    }
  }
 
  @Override
  public Collection<UserDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable pgbl = PageRequest.of(pageIndex, pageSize, orden);

    Iterable<User> iterateUsers;
    if (filters == null) {
      iterateUsers = users.findAll(pgbl);
    } else {
      iterateUsers = users.findAll(filters, pgbl);
    }

    List<UserDto> collection = new ArrayList<>();
    for (User user : iterateUsers) {
      UserDto dto = conversion.convert(user, UserDto.class);
      collection.add(dto);
    }

    return collection;
  }

  /**
   * Updates an existing user. Does not update its associated personal info.
   *
   * @param dto The item to be updated.
   *
   * @return The saved item, with updated properties, or null if the item was not found.
   */
  @Nullable
  @Override
  public UserDto update(UserDto dto) {
    LOG.debug("update({})", dto);
    Optional<User> existing = users.findById(dto.getUserId());
    if (!existing.isPresent()) {
      return null;
    } else {
      User existingUser = existing.get();
      User newUser = conversion.convert(dto, User.class);
      if (newUser.equals(existingUser)) {
        return dto;
      } else {
        Person existingPerson = existingUser.getPerson();
        newUser.setPerson(existingPerson);
        newUser = users.saveAndFlush(newUser);
        dto = conversion.convert(newUser, UserDto.class);
        PersonDto dtoPerson = conversion.convert(newUser.getPerson(), PersonDto.class);
        dto.setPerson(dtoPerson);
        return dto;
      }
    }
  }

  @Override
  public boolean delete(Long id) {
    LOG.debug("delete({})", id);
    try {
      users.deleteById(id);
      users.flush();
      return !users.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete user with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public UserDto find(Long id) {
    LOG.debug("find({})", id);
    Optional<User> userById = users.findById(id);
    if (userById.isPresent()) {
      return null;
    } else {
      return conversion.convert(userById.get(), UserDto.class);
    }
  }
}
