package cl.blm.newmarketing.backend.api.pojos;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellPojo {
  public Integer id;
  public Date date;
  public int subtotal;
  public SellTypePojo sellType;
  public ClientPojo client;
  public SellerPojo seller;
  public Collection<SellDetailPojo> sellDetails;
}
