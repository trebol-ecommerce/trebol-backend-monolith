package org.trebol.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;

import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.entities.QUser;

import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.exceptions.PersonAlreadyExistsException;
import org.trebol.jpa.exceptions.UserAlreadyExistsException;
import org.trebol.api.services.RegistrationService;
import org.trebol.jpa.repositories.CustomersRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class RegistrationServiceImpl
  implements RegistrationService {

  private final PeopleRepository peopleRepository;
  private final UsersRepository usersRepository;
  private final CustomersRepository customersRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public RegistrationServiceImpl(
      PeopleRepository peopleRepository,
      UsersRepository usersRepository,
      CustomersRepository customersRepository,
      PasswordEncoder passwordEncoder) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
    this.customersRepository = customersRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void register(RegistrationPojo registration) throws PersonAlreadyExistsException, UserAlreadyExistsException {
    String username = registration.getName();
    Predicate userWithSameName = QUser.user.name.eq(username);
    if (usersRepository.exists(userWithSameName)) {
      throw new UserAlreadyExistsException("With name '" + username + "'");
    }

    Person newPerson = this.createPersonFromRegistrationPojo(registration);
    Predicate sameProfileData = new BooleanBuilder()
        .and(QPerson.person.idCard.eq(newPerson.getIdCard()))
        .and(QPerson.person.name.eq(newPerson.getName()));
    if (peopleRepository.exists(sameProfileData)) {
      throw new PersonAlreadyExistsException("With same ID card and/or name");
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
    target.setIdCard(source.getIdCard());
    target.setEmail(source.getEmail());
    target.setAddress(source.getAddress());
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
