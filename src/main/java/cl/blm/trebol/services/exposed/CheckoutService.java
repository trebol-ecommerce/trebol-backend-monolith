package cl.blm.trebol.services.exposed;

import java.util.Collection;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CheckoutService {
  public SellPojo saveCartAsTransactionRequest(Integer clientId, Collection<SellDetailPojo> cartDetails);
  public WebPayRedirectionData startWebpayTransaction(SellPojo sellTransaction);
  public Integer confirmWebpayTransactionResult(String transactionToken);
}
