package cl.blm.trebol.services.exposed;

import java.util.Collection;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutResponsePojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutRequestPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CheckoutService {
  public WebpayCheckoutRequestPojo saveCartAsTransactionRequest(String authorization, Collection<SellDetailPojo> cartDetails);
  public WebpayCheckoutResponsePojo startWebpayTransaction(WebpayCheckoutRequestPojo webpayTransaction);
  public Integer confirmWebpayTransactionResult(String transactionToken);
}
