package cl.blm.newmarketing.backend.converters.dto2entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.dtos.SellDetailDto;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.model.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetailDto2Entity
    implements Converter<SellDetailDto, SellDetail> {

  private Product convertProduct(ProductDto source) {
    Product target = new Product();
    target.setId(source.getProductId());
    return target;
  }

  @Override
  public SellDetail convert(SellDetailDto source) {
    SellDetail target = new SellDetail();
    target.setId(source.getSellDetailId());
    target.setUnits(source.getSellDetailUnits());

    Product product = convertProduct(source.getProduct());
    target.setProduct(product);

    return target;
  }

  public Collection<SellDetail> convertCollection(Collection<SellDetailDto> source) {
    Collection<SellDetail> sellDetails = new ArrayList<>();
    for (SellDetailDto sd : source) {
      SellDetail entity = convert(sd);
      sellDetails.add(entity);
    }
    return sellDetails;
  }
}
