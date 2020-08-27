package cl.blm.newmarketing.backend.converters.dto2entity;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.SellDto;
import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.dtos.SellerDto;
import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.model.entities.SellDetail;
import cl.blm.newmarketing.backend.model.entities.SellType;
import cl.blm.newmarketing.backend.model.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDto2Entity
    implements Converter<SellDto, Sell> {

  private SellType convertSellType(SellTypeDto source) {
    SellType target = new SellType();
    target.setId(source.getSellTypeId());
    return target;
  }

  private Seller convertSeller(SellerDto source) {
    Seller target = new Seller();
    target.setId(source.getSellerId());
    return target;
  }

  @Override
  public Sell convert(SellDto source) {
    Sell target = new Sell();
    target.setId(source.getSellId());
    target.setDate(source.getSellDate());
    target.setSubtotal(source.getSellSubtotal());

    SellType sellType = convertSellType(source.getSellType());
    target.setSellType(sellType);

    Client client = (new ClientDto2Entity()).convert(source.getClient());
    target.setClient(client);

    Collection<SellDetail> sellDetails = (new SellDetailDto2Entity()).convertCollection(source.getSellDetails());
    target.setSellDetails(sellDetails);

    if (source.getSeller() != null) {
      Seller seller = convertSeller(source.getSeller());
      target.setSeller(seller);
    }

    return target;
  }
}
