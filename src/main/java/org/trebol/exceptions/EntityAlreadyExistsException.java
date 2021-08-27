package org.trebol.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class EntityAlreadyExistsException
  extends Exception {

  public EntityAlreadyExistsException() {
  }

  public EntityAlreadyExistsException(String string) {
    super(string);
  }

  public EntityAlreadyExistsException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

  public EntityAlreadyExistsException(Throwable thrwbl) {
    super(thrwbl);
  }
}
