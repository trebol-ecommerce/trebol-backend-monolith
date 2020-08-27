package cl.blm.newmarketing.backend.converters;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.SellPojo;
import cl.blm.newmarketing.backend.dtos.ClientDto;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;
import cl.blm.newmarketing.backend.dtos.SellDto;
import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.dtos.SellerDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellPojo2Dto
    implements Converter<SellPojo, SellDto> {
  @Override
  public SellDto convert(SellPojo source) {
    SellDto target = new SellDto();
    target.setSellId(source.id);
    target.setSellDate(source.date);
    target.setSellSubtotal(source.subtotal);

    SellTypeDto sellType = (new SellTypePojo2Dto()).convert(source.sellType);
    target.setSellType(sellType);

    ClientDto client = (new ClientPojo2Dto()).convert(source.client);
    target.setClient(client);

    Collection<SellDetailDto> sellDetails = (new SellDetailPojo2Dto()).convertCollection(source.sellDetails);
    target.setSellDetails(sellDetails);

    if (source.seller != null) {
      SellerDto seller = (new SellerPojo2Dto()).convert(source.seller);
      target.setSeller(seller);
    }

    return target;
  }
}
