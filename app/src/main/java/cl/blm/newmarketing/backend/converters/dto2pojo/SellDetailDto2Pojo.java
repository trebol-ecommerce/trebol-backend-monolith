package cl.blm.newmarketing.backend.converters.dto2pojo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductPojo;
import cl.blm.newmarketing.backend.api.pojos.SellDetailPojo;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetailDto2Pojo
    implements Converter<SellDetailDto, SellDetailPojo> {
  @Override
  public SellDetailPojo convert(SellDetailDto source) {
    SellDetailPojo target = new SellDetailPojo();
    target.id = source.getSellDetailId();
    target.units = source.getSellDetailUnits();

    ProductPojo product = (new ProductDto2Pojo()).convert(source.getProduct());
    target.product = product;

    return target;
  }

  public Collection<SellDetailPojo> convertCollection(Collection<SellDetailDto> source) {
    Collection<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetailDto sd : source) {
      SellDetailPojo dto = convert(sd);
      sellDetails.add(dto);
    }
    return sellDetails;
  }
}
