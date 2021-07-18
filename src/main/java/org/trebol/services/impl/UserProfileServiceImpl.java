package org.trebol.services.impl;

import java.util.Optional;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.PeopleRepository;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.services.UserProfileService;

@Service
public class UserProfileServiceImpl
    implements UserProfileService {

  private final UsersRepository usersRepository;
  private final PeopleRepository peopleRepository;

  @Autowired
  public UserProfileServiceImpl(UsersRepository usersRepository, PeopleRepository peopleRepository) {
    super();
    this.usersRepository = usersRepository;
    this.peopleRepository = peopleRepository;
  }

  @Nullable
  @Override
  public Person getProfileFromUserName(String userName) {
    Optional<User> userByName = usersRepository.findByNameWithProfile(userName);
    if (userByName.isPresent()) {
      return userByName.get().getPerson();
    } else {
      return null;
    }
  }

  @Override
  public boolean updateProfile(Person profile) {
    peopleRepository.saveAndFlush(profile);
    return true;
  }
}
