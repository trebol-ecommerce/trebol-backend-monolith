package org.trebol.jpa.services.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UsersConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<UserPojo, User> {

  private final Logger logger = LoggerFactory.getLogger(UsersConverterJpaServiceImpl.class);
  private final IUsersJpaRepository userRepository;
  private final IUserRolesJpaRepository rolesRepository;
  private final ITwoWayConverterJpaService<PersonPojo, Person> peopleService;
  private final IPeopleJpaRepository peopleRepository;
  private final ConversionService conversion;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersConverterJpaServiceImpl(IUsersJpaRepository repository,
                                      IUserRolesJpaRepository rolesRepository,
                                      ITwoWayConverterJpaService<PersonPojo, Person> peopleService,
                                      IPeopleJpaRepository peopleRepository,
                                      ConversionService conversion,
                                      PasswordEncoder passwordEncoder) {
    this.userRepository = repository;
    this.rolesRepository = rolesRepository;
    this.peopleService = peopleService;
    this.peopleRepository = peopleRepository;
    this.conversion = conversion;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Nullable
  public UserPojo convertToPojo(User source) {
    UserPojo target = new UserPojo();
    target.setId(source.getId());
    target.setName(source.getName());
    target.setRole(source.getUserRole().getName());

    Person sourcePerson = source.getPerson();
    if (sourcePerson != null) {
      PersonPojo personPojo = peopleService.convertToPojo(sourcePerson);
      target.setPerson(personPojo);
    }

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
        userById.ifPresent(user -> target.setPassword(user.getPassword()));
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
  public User applyChangesToExistingEntity(UserPojo source, User existing) throws BadInputException {
    User target = new User(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    String roleName = source.getRole();
    if (roleName != null && !roleName.isBlank() && !target.getUserRole().getName().equals(roleName)) {
      Optional<UserRole> roleNameMatch = rolesRepository.findByName(roleName);
      roleNameMatch.ifPresent(target::setUserRole);
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
        idNumberMatch.ifPresent(target::setPerson);
      }
    }

    return target;
  }
}
