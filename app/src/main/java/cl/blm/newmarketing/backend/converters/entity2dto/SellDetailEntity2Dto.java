package cl.blm.newmarketing.backend.converters.entity2dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;
import cl.blm.newmarketing.backend.model.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetailEntity2Dto
    implements Converter<SellDetail, SellDetailDto> {
  @Override
  public SellDetailDto convert(SellDetail source) {
    SellDetailDto target = new SellDetailDto();
    target.setSellDetailId(source.getId());
    target.setSellDetailUnits(source.getUnits());

    ProductDto product = (new ProductEntity2Dto()).convert(source.getProduct());
    target.setProduct(product);

    return target;
  }

  public Collection<SellDetailDto> convertCollection(Collection<SellDetail> source) {
    Collection<SellDetailDto> sellDetails = new ArrayList<>();
    for (SellDetail sd : source) {
      SellDetailDto dto = convert(sd);
      sellDetails.add(dto);
    }
    return sellDetails;
  }
}
