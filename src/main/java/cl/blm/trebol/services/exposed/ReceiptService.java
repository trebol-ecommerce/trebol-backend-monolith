package cl.blm.trebol.services.exposed;

import cl.blm.trebol.api.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface ReceiptService {
  public ReceiptPojo fetchReceiptById(int id);
}
