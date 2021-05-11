package org.trebol.api.services;

import org.trebol.api.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface ReceiptService {
  public ReceiptPojo fetchReceiptById(int id);
}
