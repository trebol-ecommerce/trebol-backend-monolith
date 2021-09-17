package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QUser;

import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;

import javassist.NotFoundException;

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

  @Override
  public UserPojo convertToPojo(User source) {
    UserPojo target = conversion.convert(source, UserPojo.class);
    return target;
  }

  @Override
  public User convertToNewEntity(UserPojo source) throws BadInputException {
    User target = conversion.convert(source, User.class);
    if (target == null) {
      throw new BadInputException("Invalid user data");
    } else {
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

      PersonPojo sourcePerson = source.getPerson();
      if (sourcePerson != null && sourcePerson.getIdNumber() != null &&
          !sourcePerson.getIdNumber().isBlank()) {
        logger.trace("Finding person profile...");
        Optional<Person> personByIdNumber = peopleRepository.findByIdNumber(sourcePerson.getIdNumber());
        if (personByIdNumber.isPresent()) {
          logger.trace("Person profile found");
          target.setPerson(personByIdNumber.get());
        }
      }

      String role = source.getRole();
      if (role != null && !role.isEmpty()){
        logger.trace("Searching user role by name '{}'...", role);
        Optional<UserRole> roleByName = rolesRepository.findByName(role);
        if (roleByName.isPresent()) {
          logger.trace("User role found");
          target.setUserRole(roleByName.get());
        } else {
          throw new BadInputException("The specified user role does not exist");
        }
      } else {
        throw new BadInputException("The user does not have a role");
      }
    }
    return target;
  }

  @Override
  public void applyChangesToExistingEntity(UserPojo source, User target) throws BadInputException {
    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    String roleName = source.getRole();
    if (roleName != null && !roleName.isBlank() && !target.getUserRole().getName().equals(roleName)) {
      Optional<UserRole> roleNameMatch = rolesRepository.findByName(roleName);
      if (roleNameMatch.isPresent()) {
        target.setUserRole(roleNameMatch.get());
      }
    }

    String password = source.getPassword();
    if (password != null && !password.isBlank() && !passwordEncoder.matches(password, target.getPassword())) {
      String encodedPassword = passwordEncoder.encode(password);
      target.setPassword(encodedPassword);
    }

    PersonPojo person = source.getPerson();
    if (person != null) {
      String idNumber = person.getIdNumber();
      if (idNumber != null && !idNumber.isBlank() && !target.getPerson().getIdNumber().equals(idNumber)) {
        Optional<Person> idNumberMatch = peopleRepository.findByIdNumber(idNumber);
        if (idNumberMatch.isPresent()) {
          target.setPerson(idNumberMatch.get());
        }
      }
    }
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QUser qUser = QUser.user;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qUser.id.eq(Long.valueOf(stringValue))); // id matching is final
          case "name":
            predicate.and(qUser.name.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(qUser.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "emailLike":
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

  @Override
  public UserPojo readOne(Long id) throws NotFoundException {
    Optional<User> userById = userRepository.findByIdWithProfile(id);
    if (!userById.isPresent()) {
      throw new NotFoundException("The requested user does not exist");
    } else {
      User found = userById.get();
      UserPojo foundPojo = this.convertToPojo(found);
      if (foundPojo != null && found.getPerson() != null) {
        PersonPojo person = conversion.convert(found.getPerson(), PersonPojo.class);
        if (person != null) {
          foundPojo.setPerson(person);
        }
      }
      return foundPojo;
    }
  }

  @Override
  public Optional<User> getExisting(UserPojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid user name");
    } else {
      return userRepository.findByName(name);
    }
  }
}
