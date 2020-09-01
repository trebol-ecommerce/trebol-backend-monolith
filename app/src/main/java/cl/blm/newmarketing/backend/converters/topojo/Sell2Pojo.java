package cl.blm.newmarketing.backend.converters.topojo;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
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
    target.setId(source.getId());
    target.setDate(source.getDate());
    target.setSubtotal(source.getSubtotal());
    target.setClient(new ClientPojo());
    target.getClient().setId(source.getClient().getId());
    target.getClient().setPerson(new PersonPojo());
    target.getClient().getPerson().setId(source.getClient().getPerson().getId());
    target.getClient().getPerson().setName(source.getClient().getPerson().getName());
    target.setSellType(new SellTypePojo());
    target.getSellType().setId(source.getSellType().getId());
    target.getSellType().setName(source.getSellType().getName());

    target.setSellDetails(new ArrayList<>());
    for (SellDetail sd : source.getSellDetails()) {
      SellDetailPojo td = new SellDetailPojo();
      td.setId(sd.getId());
      td.setUnits(sd.getUnits());
      td.setProduct(new ProductPojo());
      td.getProduct().setId(sd.getProduct().getId());
      target.getSellDetails().add(td);
    }

    if (source.getSeller() != null) {
      target.setSeller(new SellerPojo());
      target.getSeller().setId(source.getSeller().getId());
    }
    return target;
  }
}
