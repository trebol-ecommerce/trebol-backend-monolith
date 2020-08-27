package cl.blm.newmarketing.backend.dtos;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellDto {
  private Integer sellId;
  private Date sellDate;
  private int sellSubtotal;
  private SellTypeDto sellType;
  private ClientDto client;
  private SellerDto seller;
  private Collection<SellDetailDto> sellDetails;

  public SellDto() {
    super();
  }

  public Integer getSellId() {
    return sellId;
  }

  public void setSellId(Integer sellId) {
    this.sellId = sellId;
  }

  public Date getSellDate() {
    return sellDate;
  }

  public void setSellDate(Date sellDate) {
    this.sellDate = sellDate;
  }

  public int getSellSubtotal() {
    return sellSubtotal;
  }

  public void setSellSubtotal(int sellSubtotal) {
    this.sellSubtotal = sellSubtotal;
  }

  public SellTypeDto getSellType() {
    return sellType;
  }

  public void setSellType(SellTypeDto sellType) {
    this.sellType = sellType;
  }

  public ClientDto getClient() {
    return client;
  }

  public void setClient(ClientDto client) {
    this.client = client;
  }

  public SellerDto getSeller() {
    return seller;
  }

  public void setSeller(SellerDto seller) {
    this.seller = seller;
  }

  public Collection<SellDetailDto> getSellDetails() {
    return sellDetails;
  }

  public void setSellDetails(Collection<SellDetailDto> sellDetails) {
    this.sellDetails = sellDetails;
  }
}
