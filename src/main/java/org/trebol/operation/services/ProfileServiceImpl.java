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

package org.trebol.operation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.exceptions.PersonNotFoundException;
import org.trebol.jpa.exceptions.UserNotFoundException;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IPeopleConverterJpaService;
import org.trebol.jpa.services.crud.IPeopleCrudService;
import org.trebol.jpa.services.datatransport.IPeopleDataTransportJpaService;
import org.trebol.operation.IProfileService;
import org.trebol.pojo.PersonPojo;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProfileServiceImpl
    implements IProfileService {

  private final IUsersJpaRepository usersRepository;
  private final IPeopleCrudService peopleService;
  private final IPeopleConverterJpaService peopleConverter;
  private final IPeopleDataTransportJpaService peopleDataTransportService;
  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(IUsersJpaRepository usersRepository,
                            IPeopleCrudService peopleService,
                            IPeopleConverterJpaService peopleConverter,
                            IPeopleDataTransportJpaService peopleDataTransportService,
                            IPeopleJpaRepository peopleRepository) {
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
    this.peopleConverter = peopleConverter;
    this.peopleDataTransportService = peopleDataTransportService;
    this.peopleRepository = peopleRepository;
  }

  @Override
  public PersonPojo getProfileFromUserName(String userName)
      throws EntityNotFoundException {
    User user = this.getUserFromName(userName);
    Person person = user.getPerson();
    if (person == null) {
      throw new PersonNotFoundException("The account does not have an associated profile");
    } else {
      return peopleConverter.convertToPojo(person);
    }
  }

  @Transactional
  @Override
  public void updateProfileForUserWithName(String userName, PersonPojo profile)
      throws BadInputException, UserNotFoundException {
    User targetUser = this.getUserFromName(userName);
    Person target = targetUser.getPerson();
    if (target == null) {
      Optional<Person> existingProfile = peopleService.getExisting(profile);
      if (existingProfile.isPresent()) {
        Person person = existingProfile.get();
        Optional<User> userByIdNumber = usersRepository.findByPersonIdNumber(person.getIdNumber());
        if (userByIdNumber.isPresent()) {
          throw new BadInputException("Person profile is associated to another account. Cannot use it.");
        } else {
          targetUser.setPerson(person);
          usersRepository.saveAndFlush(targetUser);
        }
      } else {
        Person newProfile = peopleConverter.convertToNewEntity(profile);
        newProfile = peopleRepository.saveAndFlush(newProfile);
        targetUser.setPerson(newProfile);
        usersRepository.saveAndFlush(targetUser);
      }
    } else {
      target = peopleDataTransportService.applyChangesToExistingEntity(profile, target);
      peopleRepository.saveAndFlush(target);
    }
  }

  private User getUserFromName(String userName)
      throws UserNotFoundException {
    Optional<User> userByName = usersRepository.findByName(userName);
    if (userByName.isEmpty()) {
      throw new UserNotFoundException("There is no account with the specified username");
    } else {
      return userByName.get();
    }
  }
}
