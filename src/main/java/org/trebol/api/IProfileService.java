package org.trebol.api;

import javax.annotation.Nullable;

import org.trebol.jpa.entities.Person;

public interface IProfileService {
  
  @Nullable
  public Person getProfileFromUserName(String userName);

  public boolean updateProfile(Person profile);
}
