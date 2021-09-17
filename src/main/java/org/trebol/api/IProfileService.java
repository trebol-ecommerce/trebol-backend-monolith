package org.trebol.api;

import javassist.NotFoundException;
import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.UserNotFoundException;

public interface IProfileService {

  PersonPojo getProfileFromUserName(String userName) throws NotFoundException;

  void updateProfileForUserWithName(String userName, PersonPojo profile) throws BadInputException, UserNotFoundException;
}
