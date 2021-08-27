package org.trebol.integration.exceptions;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class PaymentServiceException
  extends Exception {
  
  public PaymentServiceException() {
  }

  public PaymentServiceException(String string) {
    super(string);
  }

  public PaymentServiceException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

  public PaymentServiceException(Throwable thrwbl) {
    super(thrwbl);
  }

}
