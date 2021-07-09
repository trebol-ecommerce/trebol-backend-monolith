package org.trebol.api.services;

import java.util.Collection;

import javax.annotation.Nullable;

import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.api.pojo.WebpayCheckoutResponsePojo;
import org.trebol.api.pojo.WebpayCheckoutRequestPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CheckoutService {

  @Nullable
  public WebpayCheckoutRequestPojo saveCartAsTransactionRequest(String authorization, Collection<SellDetailPojo> cartDetails);

  public WebpayCheckoutResponsePojo startWebpayTransaction(WebpayCheckoutRequestPojo webpayTransaction);

  @Nullable
  public Integer confirmWebpayTransactionResult(String transactionToken);
}
