package org.trebol.api;

import javax.annotation.Nullable;

import org.trebol.api.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface IReceiptService {

  @Nullable
  ReceiptPojo fetchReceiptById(long id);
}
