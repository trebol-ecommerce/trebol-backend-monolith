package org.trebol.jpa.services;

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
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UsersJpaCrudServiceImpl
  extends GenericJpaCrudService<UserPojo, User> {

  private static final Logger logger = LoggerFactory.getLogger(UsersJpaCrudServiceImpl.class);
  private final IUsersJpaRepository userRepository;
  private final IUserRolesJpaRepository rolesRepository;
  private final IPeopleJpaRepository peopleRepository;
  private final ConversionService conversion;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersJpaCrudServiceImpl(IUsersJpaRepository repository, IUserRolesJpaRepository rolesRepository,
    IPeopleJpaRepository peopleRepository, ConversionService conversion, PasswordEncoder passwordEncoder) {
    super(repository);
    this.userRepository = repository;
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
    logger.trace("Converting input user instance to entity class...", source.getRole());
    User target = conversion.convert(source, User.class);
    if (target != null) {
      if (source.getPassword() != null && !source.getPassword().isEmpty()) {
        String rawPassword = source.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        target.setPassword(encodedPassword);
      } else if (source.getId() != null) {
        // TODO optimize this! if the user exists and no password was provided, "reload" password from the database
        Optional<User> userById = userRepository.findById(source.getId());
        if (userById.isPresent()) {
          target.setPassword(userById.get().getPassword());
        }
      }

      if (source.getPerson() != null && source.getPerson().getId() != null) {
        logger.trace("Finding person profile...");
        Optional<Person> personById = peopleRepository.findById(source.getPerson().getId());
        if (personById.isPresent()) {
          logger.trace("Person profile found");
          target.setPerson(personById.get());
        } else {
          logger.error("Person profile not found");
          return null;
        }
      } else {
        logger.error("Missing required person profile");
        return null;
      }

      if (source.getRole() != null && !source.getRole().isEmpty()){
        logger.trace("Searching user role by name '{}'...", source.getRole());
        Optional<UserRole> roleByName = rolesRepository.findByName(source.getRole());
        if (roleByName.isPresent()) {
          logger.trace("User role found");
          target.setUserRole(roleByName.get());
        } else {
          logger.error("User role not found");
          return null;
        }
      } else {
        logger.error("Missing required user role");
        return null;
      }
    }
    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QUser qUser = QUser.user;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qUser.id.eq(longValue)); // id matching is final
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
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }

  @Nullable
  @Override
  public UserPojo readOne(Long id) {
    Optional<User> userById = userRepository.findByIdWithProfile(id);
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

  @Override
  public boolean itemExists(UserPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid user name");
    } else {
      return (userRepository.findByName(name).isPresent());
    }
  }
}
