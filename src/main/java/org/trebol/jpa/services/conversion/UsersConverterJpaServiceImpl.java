/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.conversion;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

@Transactional
@Service
public class UsersConverterJpaServiceImpl
  implements IUsersConverterJpaService {

  private final Logger logger = LoggerFactory.getLogger(UsersConverterJpaServiceImpl.class);
  private final IUsersJpaRepository userRepository;
  private final IUserRolesJpaRepository rolesRepository;
  private final IPeopleConverterJpaService peopleService;
  private final IPeopleJpaRepository peopleRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersConverterJpaServiceImpl(IUsersJpaRepository repository,
                                      IUserRolesJpaRepository rolesRepository,
                                      IPeopleConverterJpaService peopleService,
                                      IPeopleJpaRepository peopleRepository,
                                      PasswordEncoder passwordEncoder) {
    this.userRepository = repository;
    this.rolesRepository = rolesRepository;
    this.peopleService = peopleService;
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
      PersonPojo personPojo = peopleService.convertToPojo(sourcePerson);
      target.setPerson(personPojo);
    }
    return target;
  }

  @Override
  public User convertToNewEntity(UserPojo source) throws BadInputException {
    PersonPojo sourcePerson = source.getPerson();
    String sourceRole = source.getRole();
    User target = new User();
    target.setName(source.getName());
    this.setPersonRole(target, sourceRole);
    this.setPersonProfile(target, sourcePerson);
    this.setPassword(source, target);
    return target;
  }

  private void setPersonRole(User target, String role) throws BadInputException {
    if (StringUtils.isNotBlank(role)) {
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

  private void setPersonProfile(User target, PersonPojo sourcePerson) {
    if (sourcePerson != null && StringUtils.isNotBlank(sourcePerson.getIdNumber())) {
      logger.trace("Finding person profile...");
      Optional<Person> personByIdNumber = peopleRepository.findByIdNumber(sourcePerson.getIdNumber());
      if (personByIdNumber.isPresent()) {
        logger.trace("Person profile found");
        target.setPerson(personByIdNumber.get());
      }
    }
  }

  private void setPassword(UserPojo source, User target) {
    if (StringUtils.isNotBlank(source.getPassword())) {
      String rawPassword = source.getPassword();
      String encodedPassword = passwordEncoder.encode(rawPassword);
      target.setPassword(encodedPassword);
    } else if (source.getId() != null) {
      // TODO optimize this! if the user exists and no password was provided, "reload" password from the database
      Optional<User> userById = userRepository.findById(source.getId());
      userById.ifPresent(user -> target.setPassword(user.getPassword()));
    }
  }

  @Override
  public User applyChangesToExistingEntity(UserPojo source, User target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
