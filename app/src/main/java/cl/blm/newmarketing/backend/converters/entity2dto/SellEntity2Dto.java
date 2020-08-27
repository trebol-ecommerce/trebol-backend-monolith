package cl.blm.newmarketing.backend.converters.entity2dto;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ClientDto;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;
import cl.blm.newmarketing.backend.dtos.SellDto;
import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.dtos.SellerDto;
import cl.blm.newmarketing.backend.model.entities.Sell;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellEntity2Dto
    implements Converter<Sell, SellDto> {
  @Override
  public SellDto convert(Sell source) {
    SellDto target = new SellDto();
    target.setSellId(source.getId());
    target.setSellDate(source.getDate());
    target.setSellSubtotal(source.getSubtotal());

    SellTypeDto sellType = (new SellTypeEntity2Dto()).convert(source.getSellType());
    target.setSellType(sellType);

    ClientDto client = (new ClientEntity2Dto()).convert(source.getClient());
    target.setClient(client);

    Collection<SellDetailDto> sellDetails = (new SellDetailEntity2Dto()).convertCollection(source.getSellDetails());
    target.setSellDetails(sellDetails);

    if (source.getSeller() != null) {
      SellerDto seller = (new SellerEntity2Dto()).convert(source.getSeller());
      target.setSeller(seller);
    }

    return target;
  }
}
