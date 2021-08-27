package org.trebol.api;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.exceptions.EntityAlreadyExistsException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IRegistrationService {
  public void register(RegistrationPojo registration) throws EntityAlreadyExistsException;
}
