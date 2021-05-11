package org.trebol.api.services;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.jpa.exceptions.PersonAlreadyExistsException;
import org.trebol.jpa.exceptions.UserAlreadyExistsException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface RegistrationService {
  public void register(RegistrationPojo registration) throws PersonAlreadyExistsException, UserAlreadyExistsException;
}
