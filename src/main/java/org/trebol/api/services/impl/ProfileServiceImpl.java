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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.services.ProfileService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.exceptions.PersonNotFoundException;
import org.trebol.jpa.exceptions.UserNotFoundException;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.jpa.services.conversion.PeopleConverterService;
import org.trebol.jpa.services.crud.PeopleCrudService;
import org.trebol.jpa.services.patch.PeoplePatchService;

import java.util.Optional;

@Service
public class ProfileServiceImpl
  implements ProfileService {
  private final UsersRepository usersRepository;
  private final PeopleCrudService peopleService;
  private final PeopleConverterService peopleConverter;
  private final PeoplePatchService peoplePatchService;
  private final PeopleRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(
    UsersRepository usersRepository,
    PeopleCrudService peopleService,
    PeopleConverterService peopleConverter,
    PeoplePatchService peoplePatchService,
    PeopleRepository peopleRepository
  ) {
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
    this.peopleConverter = peopleConverter;
    this.peoplePatchService = peoplePatchService;
    this.peopleRepository = peopleRepository;
  }

  @Override
  public PersonPojo getProfileFromUserName(String userName)
    throws UserNotFoundException, PersonNotFoundException {
    Optional<User> byName = usersRepository.findByName(userName);
    if (byName.isEmpty()) {
      throw new UserNotFoundException("There is no account with the specified username");
    }
    Person person = byName.get().getPerson();
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
      target = peoplePatchService.patchExistingEntity(profile, target);
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
