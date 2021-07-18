package org.trebol.jpa.services.impl;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QUser;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.UserPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.GenericCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserCrudServiceImpl
    extends GenericCrudService<UserPojo, User, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(UserCrudServiceImpl.class);

  private final UsersRepository repository;
  private final UserRolesRepository rolesRepository;
  private final PeopleRepository peopleRepository;
  private final ConversionService conversion;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserCrudServiceImpl(
    UsersRepository repository,
    UserRolesRepository rolesRepository,
    PeopleRepository peopleRepository,
    ConversionService conversion,
    PasswordEncoder passwordEncoder
  ) {
    super(repository);
    this.repository = repository;
    this.rolesRepository = rolesRepository;
    this.peopleRepository = peopleRepository;
    this.conversion = conversion;
    this.passwordEncoder = passwordEncoder;
  }

  @Nullable
  @Override
  public UserPojo entity2Pojo(User source) {
    UserPojo target = conversion.convert(source, UserPojo.class);
    return target;
  }

  @Nullable
  @Override
  public User pojo2Entity(UserPojo source) {
    LOG.trace("Converting input user instance to entity class...", source.getRole());
    User target = conversion.convert(source, User.class);
    if (target != null) {
      if (source.getPassword() != null && !source.getPassword().isEmpty()) {
        String rawPassword = source.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        target.setPassword(encodedPassword);
      } else if (source.getId() != null) {
        // TODO optimize this! if the user exists and no password was provided, "reload" password from the database
        Optional<User> userById = repository.findById(source.getId());
        if (userById.isPresent()) {
          target.setPassword(userById.get().getPassword());
        }
      }

      if (source.getPerson() != null && source.getPerson().getId() != null) {
        LOG.trace("Finding person profile...");
        Optional<Person> personById = peopleRepository.findById(source.getPerson().getId());
        if (personById.isPresent()) {
          LOG.trace("Person profile found");
          target.setPerson(personById.get());
        } else {
          LOG.error("Person profile not found");
          return null;
        }
      } else {
        LOG.error("Missing required person profile");
        return null;
      }

      if (source.getRole() != null && !source.getRole().isEmpty()){
        LOG.trace("Searching user role by name '{}'...", source.getRole());
        Optional<UserRole> roleByName = rolesRepository.findByName(source.getRole());
        if (roleByName.isPresent()) {
          LOG.trace("User role found");
          target.setUserRole(roleByName.get());
        } else {
          LOG.error("User role not found");
          return null;
        }
      } else {
        LOG.error("Missing required user role");
        return null;
      }
    }
    return target;
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QUser qUser = QUser.user;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qUser.id.eq(intValue)); // id matching is final
          case "name":
            predicate.and(qUser.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qUser.person.email.likeIgnoreCase("%" + stringValue + "%"));
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

  @Nullable
  @Override
  public UserPojo find(Integer id) {
    Optional<User> userById = repository.findByIdWithProfile(id);
    if (!userById.isPresent()) {
      return null;
    } else {
      User found = userById.get();
      UserPojo foundPojo = this.entity2Pojo(found);
      if (foundPojo != null) {
        PersonPojo person = conversion.convert(found.getPerson(), PersonPojo.class);
        if (person != null) {
          foundPojo.setPerson(person);
        }
      }
      return foundPojo;
    }
  }
}
