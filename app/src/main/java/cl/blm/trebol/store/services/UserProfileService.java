package cl.blm.trebol.store.services;

import javax.annotation.Nullable;

import cl.blm.trebol.store.jpa.entities.Person;

public interface UserProfileService {
  @Nullable
  public Person getProfileFromUserName(String userName);

  @Nullable
  public boolean updateProfile(Person profile);
}
