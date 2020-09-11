package cl.blm.newmarketing.backend.converters.toentity;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.jpa.entities.Client;
import cl.blm.newmarketing.backend.jpa.entities.Product;
import cl.blm.newmarketing.backend.jpa.entities.Sell;
import cl.blm.newmarketing.backend.jpa.entities.SellDetail;
import cl.blm.newmarketing.backend.jpa.entities.SellType;
import cl.blm.newmarketing.backend.jpa.entities.Seller;

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
    target.setSubtotal(source.getSubtotal());
    target.setClient(new Client(source.getClient().getId()));
    target.setSellType(new SellType(source.getSellType().getId()));

    target.setSellDetails(new ArrayList<>());
    for (SellDetailPojo sd : source.getSellDetails()) {
      SellDetail td = new SellDetail(sd.getId());
      td.setUnits(sd.getUnits());
      td.setProduct(new Product(sd.getProduct().getId()));
      target.getSellDetails().add(td);
    }

    if (source.getSeller() != null) {
      target.setSeller(new Seller(source.getSeller().getId()));
    }
    return target;
  }
}
