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
  public WebpayTransactionPojo saveCartAsTransactionRequest(String authorization, Collection<SellDetailPojo> cartDetails);
  public WebPayRedirectionData startWebpayTransaction(WebpayTransactionPojo webpayTransaction);
  public Integer confirmWebpayTransactionResult(String transactionToken);
}
