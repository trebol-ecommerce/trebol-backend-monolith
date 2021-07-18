package org.trebol.services;

import javax.annotation.Nullable;

import org.trebol.jpa.entities.Person;

public interface UserProfileService {
  
  @Nullable
  public Person getProfileFromUserName(String userName);

  public boolean updateProfile(Person profile);
}
