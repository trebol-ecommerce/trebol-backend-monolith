package org.trebol.operation;

import org.trebol.pojo.ReceiptPojo;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface IReceiptService {
  ReceiptPojo fetchReceiptByTransactionToken(String token) throws NotFoundException;
}
