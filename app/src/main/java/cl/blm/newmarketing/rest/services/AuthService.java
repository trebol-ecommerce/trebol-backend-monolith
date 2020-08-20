package cl.blm.newmarketing.rest.services;

import cl.blm.newmarketing.pojos.LoginPojo;

//TODO document this class

/**
 * Interface for a service that handles creating, validating and invalidating session tokens.
 * Should be used along with a service that stores actual Session instances.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface AuthService {
  public Long identifyUser(LoginPojo credentials);

  public String generateToken(Long userId);

  public boolean validateToken(String hash);

  public boolean killToken(String hash);
}
