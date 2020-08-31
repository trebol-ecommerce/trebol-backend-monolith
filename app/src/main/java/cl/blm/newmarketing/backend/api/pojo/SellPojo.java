package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;
import java.util.Date;

public class SellPojo {
  public Integer id;
  public Date date;
  public int subtotal;
  public SellTypePojo sellType;
  public ClientPojo client;
  public SellerPojo seller;
  public Collection<SellDetailPojo> sellDetails;
}
