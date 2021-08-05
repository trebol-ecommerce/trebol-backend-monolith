package org.trebol.converters.toentity;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.api.pojo.SellPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.entities.SellType;
import org.trebol.jpa.entities.Salesperson;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Sell2Entity
    implements Converter<SellPojo, Sell> {

  @Override
  public Sell convert(SellPojo source) {
    Sell target = new Sell();
    target.setId(source.getId());
    target.setDate(source.getDate());
    target.setTotalValue(source.getSubtotal());

    Customer targetCustomer = new Customer();
    targetCustomer.setId(source.getCustomer().getId());
    target.setCustomer(targetCustomer);

    SellType targetSellType = new SellType();
    targetSellType.setId(source.getSellType().getId());
    target.setType(targetSellType);

    target.setDetails(new ArrayList<>());
    for (SellDetailPojo sd : source.getSellDetails()) {
      SellDetail td = new SellDetail();
      td.setId(sd.getId());
      td.setUnits(sd.getUnits());
      Product targetProduct = new Product();
      targetProduct.setId(sd.getProduct().getId());
      td.setProduct(targetProduct);
      target.getDetails().add(td);
    }

    if (source.getSalesperson() != null) {
      Salesperson targetSalesperson = new Salesperson();
      targetSalesperson.setId(source.getSalesperson().getId());
      target.setSalesperson(targetSalesperson);
    }
    return target;
  }
}
