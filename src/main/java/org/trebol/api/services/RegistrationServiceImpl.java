package org.trebol.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import org.trebol.pojo.RegistrationPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;

import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.entities.QUser;

import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.api.IRegistrationService;
import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class RegistrationServiceImpl
  implements IRegistrationService {

  private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
  private final IPeopleJpaRepository peopleRepository;
  private final IUsersJpaRepository usersRepository;
  private final IUserRolesJpaRepository rolesRepository;
  private final ICustomersJpaRepository customersRepository;
  private final PasswordEncoder passwordEncoder;
  private final ConversionService conversionService;

  @Autowired
  public RegistrationServiceImpl(IPeopleJpaRepository peopleRepository,
    IUsersJpaRepository usersRepository, IUserRolesJpaRepository rolesRepository,
    ICustomersJpaRepository customersRepository, PasswordEncoder passwordEncoder,
    ConversionService conversionService) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
    this.rolesRepository = rolesRepository;
    this.customersRepository = customersRepository;
    this.passwordEncoder = passwordEncoder;
    this.conversionService = conversionService;
  }

  @Override
  public void register(RegistrationPojo registration) throws BadInputException, EntityAlreadyExistsException {
    String username = registration.getName();
    Predicate userWithSameName = QUser.user.name.eq(username);
    if (usersRepository.exists(userWithSameName)) {
      throw new EntityAlreadyExistsException("That username is taken.");
    }

    PersonPojo sourcePerson = registration.getProfile();
    Person newPerson = conversionService.convert(sourcePerson, Person.class);
    if (newPerson == null) {
      throw new BadInputException("Input profile has insufficient or invalid data.");
    }

    Predicate sameProfileData = QPerson.person.idNumber.eq(newPerson.getIdNumber());
    if (peopleRepository.exists(sameProfileData)) {
      throw new EntityAlreadyExistsException("That ID number is already registered and associated to an account.");
    } else {
      newPerson = peopleRepository.saveAndFlush(newPerson);
    }

    User newUser = this.convertToUser(registration);
    newUser.setPerson(newPerson);
    usersRepository.saveAndFlush(newUser);
    logger.info("New user created with name '{}' and idNumber '{}'", newUser.getName(), newPerson.getIdNumber());

    Customer newCustomer = new Customer();
    newCustomer.setPerson(newPerson);
    customersRepository.saveAndFlush(newCustomer);
  }

  protected User convertToUser(RegistrationPojo registration) {
    String password = passwordEncoder.encode(registration.getPassword());
    User target = new User();
    target.setName(registration.getName());
    target.setPassword(password);

    Optional<UserRole> customerRole = rolesRepository.findByName("Customer");
    target.setUserRole(customerRole.get());
    return target;
  }

}
