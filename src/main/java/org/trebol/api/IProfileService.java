package org.trebol.api;

import javax.annotation.Nullable;

import javassist.NotFoundException;
import org.trebol.api.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.UserNotFoundException;
import org.trebol.jpa.entities.Person;

public interface IProfileService {

  public PersonPojo getProfileFromUserName(String userName) throws NotFoundException;

  public void updateProfileForUserWithName(String userName, PersonPojo profile) throws BadInputException, UserNotFoundException;
}
