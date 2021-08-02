package org.trebol.jpa.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class PersonAlreadyExistsException
  extends EntityAlreadyExistsException {

  public PersonAlreadyExistsException() {
  }

  public PersonAlreadyExistsException(String string) {
    super(string);
  }

  public PersonAlreadyExistsException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

  public PersonAlreadyExistsException(Throwable thrwbl) {
    super(thrwbl);
  }
}
