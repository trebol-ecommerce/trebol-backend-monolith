package org.trebol.services.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class UserAlreadyExistsException
  extends Exception {

  public UserAlreadyExistsException() {
  }

  public UserAlreadyExistsException(String string) {
    super(string);
  }

  public UserAlreadyExistsException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

  public UserAlreadyExistsException(Throwable thrwbl) {
    super(thrwbl);
  }
}
