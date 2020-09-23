package cl.blm.newmarketing.store.services;

import javax.annotation.Nullable;

import cl.blm.newmarketing.store.jpa.entities.Person;

public interface UserProfileService {
  @Nullable
  public Person getProfileFromUserName(String userName);

  @Nullable
  public boolean updateProfile(Person profile);
}
