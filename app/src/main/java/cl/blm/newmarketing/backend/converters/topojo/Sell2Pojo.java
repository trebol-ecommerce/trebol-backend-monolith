package cl.blm.newmarketing.backend.converters.topojo;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.api.pojo.SellerPojo;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.model.entities.SellDetail;

@Component
public class Sell2Pojo
    implements Converter<Sell, SellPojo> {

  @Override
  public SellPojo convert(Sell source) {
    SellPojo target = new SellPojo();
    target.id = source.getId();
    target.date = source.getDate();
    target.subtotal = source.getSubtotal();
    target.client = new ClientPojo();
    target.client.id = source.getClient().getId();
    target.sellType = new SellTypePojo();
    target.sellType.id = source.getSellType().getId();

    target.sellDetails = new ArrayList<>();
    for (SellDetail sd : source.getSellDetails()) {
      SellDetailPojo td = new SellDetailPojo();
      td.id = sd.getId();
      td.units = sd.getUnits();
      td.product = new ProductPojo();
      td.product.id = sd.getProduct().getId();
      target.sellDetails.add(td);
    }

    if (source.getSeller() != null) {
      target.seller = new SellerPojo();
      target.seller.id = source.getSeller().getId();
    }
    return target;
  }
}
