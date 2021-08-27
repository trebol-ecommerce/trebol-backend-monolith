package org.trebol.api;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IRegistrationService {
  void register(RegistrationPojo registration) throws BadInputException, EntityAlreadyExistsException;
}
