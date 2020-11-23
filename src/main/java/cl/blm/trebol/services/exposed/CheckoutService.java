package cl.blm.trebol.services.exposed;

import java.util.Collection;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.api.pojo.WebpayTransactionPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CheckoutService {
  public WebpayTransactionPojo saveCartAsTransactionRequest(Integer clientId, Collection<SellDetailPojo> cartDetails);
  public WebPayRedirectionData startWebpayTransaction(WebpayTransactionPojo sellTransaction);
  public Integer confirmWebpayTransactionResult(String transactionToken);
}
