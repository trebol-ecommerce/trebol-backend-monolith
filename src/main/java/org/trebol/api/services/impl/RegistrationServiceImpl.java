/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.services.impl;

import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.RegistrationPojo;
import org.trebol.api.services.RegistrationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.CustomersRepository;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.conversion.PeopleConverterService;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@Service
public class RegistrationServiceImpl
  implements RegistrationService {
  private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
  private final PeopleRepository peopleRepository;
  private final UsersRepository usersRepository;
  private final UserRolesRepository rolesRepository;
  private final CustomersRepository customersRepository;
  private final PasswordEncoder passwordEncoder;
  private final PeopleConverterService peopleConverterService;

  @Autowired
  public RegistrationServiceImpl(
    PeopleRepository peopleRepository,
    UsersRepository usersRepository,
    UserRolesRepository rolesRepository,
    CustomersRepository customersRepository,
    PasswordEncoder passwordEncoder,
    PeopleConverterService peopleConverterService
  ) {
    this.peopleRepository = peopleRepository;
    this.usersRepository = usersRepository;
    this.rolesRepository = rolesRepository;
    this.customersRepository = customersRepository;
    this.passwordEncoder = passwordEncoder;
    this.peopleConverterService = peopleConverterService;
  }

  @Override
  public void register(RegistrationPojo registration)
    throws BadInputException, EntityExistsException {
    String username = registration.getName();
    Predicate userWithSameName = QUser.user.name.eq(username);
    if (usersRepository.exists(userWithSameName)) {
      throw new EntityExistsException("That username is taken.");
    }

    PersonPojo sourcePerson = registration.getProfile();
    Person newPerson = peopleConverterService.convertToNewEntity(sourcePerson);

    Predicate sameProfileData = QPerson.person.idNumber.eq(sourcePerson.getIdNumber());
    if (peopleRepository.exists(sameProfileData)) {
      throw new EntityExistsException("That ID number is already registered and associated to an account.");
    } else {
      newPerson = peopleRepository.saveAndFlush(newPerson);
    }

    User newUser = this.convertToUser(registration);
    newUser.setPerson(newPerson);
    usersRepository.saveAndFlush(newUser);
    logger.info("New user created with name '{}' and idNumber '{}'", newUser.getName(), newPerson.getIdNumber());

    Customer newCustomer = Customer.builder()
      .person(newPerson)
      .build();
    customersRepository.saveAndFlush(newCustomer);
  }

  protected User convertToUser(RegistrationPojo registration) {
    String password = passwordEncoder.encode(registration.getPassword());
    User target = User.builder()
      .name(registration.getName())
      .password(password)
      .build();

    Optional<UserRole> customerRole = rolesRepository.findByName("Customer");
    if (customerRole.isEmpty()) {
      throw new IllegalStateException("No user role matches 'Customer', database might be compromised");
    } else {
      target.setUserRole(customerRole.get());
    }
    return target;
  }
}
