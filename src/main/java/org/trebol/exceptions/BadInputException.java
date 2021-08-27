package org.trebol.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class BadInputException
  extends Exception {

  public BadInputException() {
  }

  public BadInputException(String string) {
    super(string);
  }

  public BadInputException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

  public BadInputException(Throwable thrwbl) {
    super(thrwbl);
  }
}
