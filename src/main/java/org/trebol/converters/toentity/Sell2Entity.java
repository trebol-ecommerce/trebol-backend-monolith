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
    Sell target = new Sell(source.getId());
    target.setDate(source.getDate());
    target.setTotalValue(source.getSubtotal());
    target.setCustomer(new Customer(source.getCustomer().getId()));
    target.setType(new SellType(source.getSellType().getId()));

    target.setDetails(new ArrayList<>());
    for (SellDetailPojo sd : source.getSellDetails()) {
      SellDetail td = new SellDetail(sd.getId());
      td.setUnits(sd.getUnits());
      td.setProduct(new Product(sd.getProduct().getId()));
      target.getDetails().add(td);
    }

    if (source.getSalesperson() != null) {
      target.setSalesperson(new Salesperson(source.getSalesperson().getId()));
    }
    return target;
  }
}
