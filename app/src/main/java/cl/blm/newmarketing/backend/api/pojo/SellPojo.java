package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public Date date;
  @JsonInclude
  public int subtotal;
  @JsonInclude
  public SellTypePojo sellType;
  @JsonInclude
  public ClientPojo client;
  @JsonInclude
  public SellerPojo seller;
  @JsonInclude(value = Include.NON_EMPTY)
  public Collection<SellDetailPojo> sellDetails;
}
