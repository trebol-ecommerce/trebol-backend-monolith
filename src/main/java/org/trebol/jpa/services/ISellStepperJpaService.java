package org.trebol.jpa.services;

import javassist.NotFoundException;

/**
 * Defines methods to progress through transaction stages (like series of steps).
 */
public interface ISellStepperJpaService {
  void setSellStatusToPaymentStartedWithToken(Long id, String token) throws NotFoundException;
  void setSellStatusToPaymentAborted(Long id) throws NotFoundException;
  void setSellStatusToPaymentFailed(Long id) throws NotFoundException;
  void setSellStatusToPaidUnconfirmed(Long id) throws NotFoundException;
  void setSellStatusToPaidConfirmed(Long id) throws NotFoundException;
  void setSellStatusToRejected(Long id) throws NotFoundException;
  void setSellStatusToCompleted(Long id) throws NotFoundException;
}
