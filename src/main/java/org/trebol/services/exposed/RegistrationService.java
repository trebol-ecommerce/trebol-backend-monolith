package org.trebol.services.exposed;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.services.exceptions.PersonAlreadyExistsException;
import org.trebol.services.exceptions.UserAlreadyExistsException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface RegistrationService {
  public void register(RegistrationPojo registration) throws PersonAlreadyExistsException, UserAlreadyExistsException;
}
