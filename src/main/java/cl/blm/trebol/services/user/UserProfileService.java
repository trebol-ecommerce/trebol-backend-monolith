package cl.blm.trebol.services.user;

import javax.annotation.Nullable;

import cl.blm.trebol.jpa.entities.Person;

public interface UserProfileService {
  @Nullable
  public Person getProfileFromUserName(String userName);

  @Nullable
  public boolean updateProfile(Person profile);
}
