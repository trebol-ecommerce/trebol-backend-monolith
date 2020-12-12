package cl.blm.trebol.services.exposed;

import cl.blm.trebol.api.pojo.RegistrationPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface RegistrationService {
  public boolean register(RegistrationPojo registration);
}
