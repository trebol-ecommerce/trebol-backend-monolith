package org.trebol.api.services;

import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.PersonNotFoundException;
import org.trebol.exceptions.UserNotFoundException;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.api.IProfileService;

@Service
public class ProfileServiceImpl
    implements IProfileService {

  private final IUsersJpaRepository usersRepository;
  private final GenericJpaCrudService<PersonPojo, Person> peopleService;
  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(IUsersJpaRepository usersRepository,
                            GenericJpaCrudService<PersonPojo, Person> peopleService,
                            IPeopleJpaRepository peopleRepository) {
    super();
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
    this.peopleRepository = peopleRepository;
  }

  @Override
  public PersonPojo getProfileFromUserName(String userName) throws NotFoundException {
    Person person = this.getPersonEntityFromRelatedUserName(userName);
    return peopleService.convertToPojo(person);
  }

  @Override
  public void updateProfileForUserWithName(String userName, PersonPojo profile)
          throws BadInputException, UserNotFoundException {
    Person target;
    try {
      target = this.getPersonEntityFromRelatedUserName(userName);
    } catch (PersonNotFoundException ex) {
      Optional<Person> existingProfile = peopleService.getExisting(profile);
      if (existingProfile.isPresent()) {
        throw new BadInputException("Person profile already has another account, please log in to that one.");
      } else {
        target = peopleService.convertToNewEntity(profile);
      }
    }
    peopleService.applyChangesToExistingEntity(profile, target);
    peopleRepository.saveAndFlush(target);
  }

  private Person getPersonEntityFromRelatedUserName(String userName)
          throws UserNotFoundException, PersonNotFoundException {
    Optional<User> userByName = usersRepository.findByName(userName);
    if (userByName.isEmpty()) {
      throw new UserNotFoundException("There is no account with the specified username");
    } else if (userByName.get().getPerson() == null) {
      throw new PersonNotFoundException("The specified user does not have a person profile associated to it");
    } else {
      return userByName.get().getPerson();
    }
  }
}
