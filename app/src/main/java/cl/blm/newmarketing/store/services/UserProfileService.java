package cl.blm.newmarketing.store.services;

import java.util.Optional;

import cl.blm.newmarketing.store.jpa.entities.Person;

public interface UserProfileService {
  public Optional<Person> getProfileFromUserName(String userName);
}
