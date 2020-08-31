package cl.blm.newmarketing.backend.converters.toentity;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.model.entities.SellDetail;
import cl.blm.newmarketing.backend.model.entities.SellType;
import cl.blm.newmarketing.backend.model.entities.Seller;

@Component
public class Sell2Entity
    implements Converter<SellPojo, Sell> {

  @Override
  public Sell convert(SellPojo source) {
    Sell target = new Sell(source.id);
    target.setDate(source.date);
    target.setSubtotal(source.subtotal);
    target.setClient(new Client(source.client.id));
    target.setSellType(new SellType(source.sellType.id));

    target.setSellDetails(new ArrayList<>());
    for (SellDetailPojo sd : source.sellDetails) {
      SellDetail td = new SellDetail(sd.id);
      td.setUnits(sd.units);
      td.setProduct(new Product(sd.product.id));
      target.getSellDetails().add(td);
    }

    if (source.seller != null) {
      target.setSeller(new Seller(source.seller.id));
    }
    return target;
  }
}
