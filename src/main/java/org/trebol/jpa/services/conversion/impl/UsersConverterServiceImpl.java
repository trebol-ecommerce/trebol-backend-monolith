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

package org.trebol.jpa.services.conversion.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UserRolesRepository;
import org.trebol.jpa.services.conversion.PeopleConverterService;
import org.trebol.jpa.services.conversion.UsersConverterService;

import java.util.Optional;

@Transactional
@Service
public class UsersConverterServiceImpl
  implements UsersConverterService {
  private final Logger logger = LoggerFactory.getLogger(UsersConverterServiceImpl.class);
  private final UserRolesRepository rolesRepository;
  private final PeopleConverterService peopleConverterService;
  private final PeopleRepository peopleRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersConverterServiceImpl(
    UserRolesRepository rolesRepository,
    PeopleConverterService peopleConverterService,
    PeopleRepository peopleRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.rolesRepository = rolesRepository;
    this.peopleConverterService = peopleConverterService;
    this.peopleRepository = peopleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserPojo convertToPojo(User source) {
    UserPojo target = UserPojo.builder()
      .id(source.getId())
      .name(source.getName())
      .role(source.getUserRole().getName())
      .build();
    Person sourcePerson = source.getPerson();
    if (sourcePerson != null) {
      PersonPojo personPojo = peopleConverterService.convertToPojo(sourcePerson);
      target.setPerson(personPojo);
    }
    return target;
  }

  @Override
  public User convertToNewEntity(UserPojo source) throws BadInputException {
    User target = User.builder()
      .name(source.getName())
      .build();

    String sourceRole = source.getRole();
    if (StringUtils.isBlank(sourceRole)) {
      throw new BadInputException("The user was not given any role");
    }
    Optional<UserRole> roleByName = rolesRepository.findByName(sourceRole);
    if (roleByName.isPresent()) {
      logger.trace("User role found");
      target.setUserRole(roleByName.get());
    } else {
      throw new BadInputException("The specified user role does not exist");
    }

    PersonPojo sourcePerson = source.getPerson();
    if (sourcePerson != null && StringUtils.isNotBlank(sourcePerson.getIdNumber())) {
      logger.trace("Finding person profile...");
      Optional<Person> personByIdNumber = peopleRepository.findByIdNumber(sourcePerson.getIdNumber());
      if (personByIdNumber.isPresent()) {
        logger.trace("Person profile found");
        target.setPerson(personByIdNumber.get());
      }
    }

    if (StringUtils.isNotBlank(source.getPassword())) {
      String rawPassword = source.getPassword();
      String encodedPassword = passwordEncoder.encode(rawPassword);
      target.setPassword(encodedPassword);
    }
    return target;
  }

  @Override
  public User applyChangesToExistingEntity(UserPojo source, User target) {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
