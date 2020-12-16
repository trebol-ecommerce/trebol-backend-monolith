package cl.blm.trebol.services.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class PersonAlreadyExistsException
  extends Exception {

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
