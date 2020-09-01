package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public Date date;
  @JsonInclude
  @NotNull
  public int subtotal;
  @JsonInclude
  @NotNull
  @Valid
  public SellTypePojo sellType;
  @JsonInclude
  @NotNull
  @Valid
  public ClientPojo client;
  @JsonInclude
  @Nullable
  @Valid
  public SellerPojo seller;
  @JsonInclude(value = Include.NON_EMPTY)
  @NotEmpty
  @Valid
  public Collection<SellDetailPojo> sellDetails;
}
