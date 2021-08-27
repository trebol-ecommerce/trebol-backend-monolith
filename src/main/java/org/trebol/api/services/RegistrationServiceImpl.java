package org.trebol.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;

import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.entities.QUser;

import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.api.IRegistrationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class RegistrationServiceImpl
  implements IRegistrationService {

  private final IPeopleJpaRepository peopleRepository;
  private final IUsersJpaRepository usersRepository;
  private final ICustomersJpaRepository customersRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public RegistrationServiceImpl(
      IPeopleJpaRepository peopleRepository,
      IUsersJpaRepository usersRepository,
      ICustomersJpaRepository customersRepository,
      PasswordEncoder passwordEncoder) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
    this.customersRepository = customersRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void register(RegistrationPojo registration) throws EntityAlreadyExistsException {
    String username = registration.getName();
    Predicate userWithSameName = QUser.user.name.eq(username);
    if (usersRepository.exists(userWithSameName)) {
      throw new EntityAlreadyExistsException("User with name '" + username + "'");
    }

    Person newPerson = this.createPersonFromRegistrationPojo(registration);
    Predicate sameProfileData = new BooleanBuilder()
        .and(QPerson.person.idNumber.eq(newPerson.getIdNumber()))
        .and(QPerson.person.name.eq(newPerson.getName()));
    if (peopleRepository.exists(sameProfileData)) {
      throw new EntityAlreadyExistsException("Person with same ID card and/or name");
    } else {
      newPerson = peopleRepository.saveAndFlush(newPerson);
    }

    User newUser = this.createUserFromRegistrationPojo(registration);
    newUser.setPerson(newPerson);
    usersRepository.saveAndFlush(newUser);

    Customer newCustomer = this.createCustomerFromRegistrationPojo(newPerson);
    customersRepository.saveAndFlush(newCustomer);
  }

  protected Person createPersonFromRegistrationPojo(RegistrationPojo registration) {
    RegistrationPojo.Profile source = registration.getProfile();
    Person target = new Person();
    target.setName(source.getName());
    target.setIdNumber(source.getIdNumber());
    target.setEmail(source.getEmail());
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }

  protected User createUserFromRegistrationPojo(RegistrationPojo registration) {
    String password = passwordEncoder.encode(registration.getPassword());
    User target = new User();
    target.setName(registration.getName());
    target.setPassword(password);

    UserRole userRole = new UserRole();
    userRole.setId(1L);
    target.setUserRole(userRole);
    return target;
  }

  protected Customer createCustomerFromRegistrationPojo(Person person) {
    Customer target = new Customer();
    target.setPerson(person);
    return target;
  }

}
