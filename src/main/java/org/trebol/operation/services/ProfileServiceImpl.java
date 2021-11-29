package org.trebol.operation.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.PersonNotFoundException;
import org.trebol.exceptions.UserNotFoundException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.operation.IProfileService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

@Service
public class ProfileServiceImpl
    implements IProfileService {

  private final IUsersJpaRepository usersRepository;
  private final GenericCrudJpaService<PersonPojo, Person> peopleService;
  private final ITwoWayConverterJpaService<PersonPojo, Person> peopleConverter;
  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(IUsersJpaRepository usersRepository,
                            GenericCrudJpaService<PersonPojo, Person> peopleService,
                            ITwoWayConverterJpaService<PersonPojo, Person> peopleConverter,
                            IPeopleJpaRepository peopleRepository) {
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
    this.peopleConverter = peopleConverter;
    this.peopleRepository = peopleRepository;
  }

  @Override
  public PersonPojo getProfileFromUserName(String userName) throws NotFoundException {
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
      target = peopleConverter.applyChangesToExistingEntity(profile, target);
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
