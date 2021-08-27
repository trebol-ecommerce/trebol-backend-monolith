package org.trebol.api.services;

import java.util.Optional;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.api.IProfileService;

@Service
public class ProfileServiceImpl
    implements IProfileService {

  private final IUsersJpaRepository usersRepository;
  private final IPeopleJpaRepository peopleRepository;

  @Autowired
  public ProfileServiceImpl(IUsersJpaRepository usersRepository, IPeopleJpaRepository peopleRepository) {
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
