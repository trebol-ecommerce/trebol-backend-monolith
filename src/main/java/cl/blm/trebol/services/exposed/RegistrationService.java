package cl.blm.trebol.services.exposed;

import cl.blm.trebol.api.pojo.RegistrationPojo;
import cl.blm.trebol.services.exceptions.PersonAlreadyExistsException;
import cl.blm.trebol.services.exceptions.UserAlreadyExistsException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface RegistrationService {
  public void register(RegistrationPojo registration) throws PersonAlreadyExistsException, UserAlreadyExistsException;
}
