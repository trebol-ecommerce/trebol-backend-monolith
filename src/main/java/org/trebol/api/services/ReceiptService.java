package org.trebol.api.services;

import javax.annotation.Nullable;

import org.trebol.api.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface ReceiptService {

  @Nullable
  ReceiptPojo fetchReceiptById(int id);
}
