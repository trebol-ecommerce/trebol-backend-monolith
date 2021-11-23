package org.trebol.jpa;

import org.trebol.pojo.SellPojo;

import com.querydsl.core.types.Predicate;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface ISalesJpaService
  extends IJpaCrudService<SellPojo, Long> {

  void setSellStatusToPaymentStartedWithToken(Long id, String token) throws NotFoundException;
  void setSellStatusToPaymentAborted(Long id) throws NotFoundException;
  void setSellStatusToPaymentFailed(Long id) throws NotFoundException;
  void setSellStatusToPaidUnconfirmed(Long id) throws NotFoundException;
  void setSellStatusToPaidConfirmed(Long id) throws NotFoundException;
  void setSellStatusToRejected(Long id) throws NotFoundException;
  void setSellStatusToCompleted(Long id) throws NotFoundException;
}
