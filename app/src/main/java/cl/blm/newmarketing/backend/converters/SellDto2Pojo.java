package cl.blm.newmarketing.backend.converters;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ClientPojo;
import cl.blm.newmarketing.backend.api.pojos.SellDetailPojo;
import cl.blm.newmarketing.backend.api.pojos.SellPojo;
import cl.blm.newmarketing.backend.api.pojos.SellTypePojo;
import cl.blm.newmarketing.backend.api.pojos.SellerPojo;
import cl.blm.newmarketing.backend.dtos.SellDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDto2Pojo
    implements Converter<SellDto, SellPojo> {
  @Override
  public SellPojo convert(SellDto source) {
    SellPojo target = new SellPojo();
    target.id = source.getSellId();
    target.date = source.getSellDate();
    target.subtotal = source.getSellSubtotal();

    SellTypePojo sellType = (new SellTypeDto2Pojo()).convert(source.getSellType());
    target.sellType = sellType;

    ClientPojo client = (new ClientDto2Pojo()).convert(source.getClient());
    target.client = client;

    Collection<SellDetailPojo> sellDetails = (new SellDetailDto2Pojo()).convertCollection(source.getSellDetails());
    target.sellDetails = sellDetails;

    if (source.getSeller() != null) {
      SellerPojo seller = (new SellerDto2Pojo()).convert(source.getSeller());
      target.seller = seller;
    }

    return target;
  }
}
