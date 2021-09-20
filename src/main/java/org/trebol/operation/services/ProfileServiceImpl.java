package org.trebol.operation.services;

import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.PersonNotFoundException;
import org.trebol.exceptions.UserNotFoundException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.operation.IProfileService;

@Service
public class ProfileServiceImpl
    implements IProfileService {

  private final IUsersJpaRepository usersRepository;
  private final GenericJpaService<PersonPojo, Person> peopleService;
  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(IUsersJpaRepository usersRepository,
                            GenericJpaService<PersonPojo, Person> peopleService,
                            IPeopleJpaRepository peopleRepository) {
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
    this.peopleRepository = peopleRepository;
  }

  @Override
  public PersonPojo getProfileFromUserName(String userName) throws NotFoundException {
    User user = this.getUserFromName(userName);
    Person person = user.getPerson();
    if (person == null) {
      throw new PersonNotFoundException("The account does not have an associated profile");
    } else {
      return peopleService.convertToPojo(person);
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
        Person newProfile = peopleService.convertToNewEntity(profile);
        newProfile = peopleRepository.saveAndFlush(newProfile);
        targetUser.setPerson(newProfile);
        usersRepository.saveAndFlush(targetUser);
      }
    } else {
      target = peopleService.applyChangesToExistingEntity(profile, target);
      peopleRepository.saveAndFlush(target);
    }
  }

  private User getUserFromName(String userName) throws UserNotFoundException {
    Optional<User> userByName = usersRepository.findByName(userName);
    if (userByName.isEmpty()) {
      throw new UserNotFoundException("There is no account with the specified username");
    } else {
      return userByName.get();
    }
  }
}
