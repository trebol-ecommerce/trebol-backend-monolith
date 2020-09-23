package cl.blm.newmarketing.store.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import cl.blm.newmarketing.store.jpa.entities.Person;
import cl.blm.newmarketing.store.jpa.entities.User;
import cl.blm.newmarketing.store.jpa.repositories.UsersRepository;
import cl.blm.newmarketing.store.services.UserProfileService;

public class UserProfileServiceImpl
    implements UserProfileService {

  @Autowired
  private UsersRepository repository;

  @Override
  public Person getProfileFromUserName(String userName) {
    Optional<User> userByName = repository.findByNameWithProfile(userName);
    Person target = userByName.get().getPerson();
    return target;
  }
}
