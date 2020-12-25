package cl.blm.trebol.converters.toentity;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.jpa.entities.Product;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.jpa.entities.SellDetail;
import cl.blm.trebol.jpa.entities.SellType;
import cl.blm.trebol.jpa.entities.Salesperson;

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

    if (source.getSeller() != null) {
      target.setSalesperson(new Salesperson(source.getSeller().getId()));
    }
    return target;
  }
}
