package org.trebol.api;

import org.trebol.pojo.ReceiptPojo;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface IReceiptService {
  ReceiptPojo fetchReceiptById(long id) throws NotFoundException;
}
