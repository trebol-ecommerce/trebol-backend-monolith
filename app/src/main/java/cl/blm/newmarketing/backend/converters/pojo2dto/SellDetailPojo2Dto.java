package cl.blm.newmarketing.backend.converters.pojo2dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.SellDetailPojo;
import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetailPojo2Dto
    implements Converter<SellDetailPojo, SellDetailDto> {
  @Override
  public SellDetailDto convert(SellDetailPojo source) {
    SellDetailDto target = new SellDetailDto();

    if (source.id != null) {
      target.setSellDetailId(source.id);
    }

    if (source.units > 0) {
      target.setSellDetailUnits(source.units);
    }

    if (source.product != null) {
      ProductDto product = (new ProductPojo2Dto()).convert(source.product);
      target.setProduct(product);
    }

    return target;
  }

  public Collection<SellDetailDto> convertCollection(Collection<SellDetailPojo> source) {
    Collection<SellDetailDto> sellDetails = new ArrayList<>();
    for (SellDetailPojo sd : source) {
      SellDetailDto dto = convert(sd);
      sellDetails.add(dto);
    }
    return sellDetails;
  }
}
