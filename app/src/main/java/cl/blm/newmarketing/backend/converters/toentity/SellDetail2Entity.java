package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.model.entities.SellDetail;

@Component
public class SellDetail2Entity
    implements Converter<SellDetailPojo, SellDetail> {

  @Override
  public SellDetail convert(SellDetailPojo source) {
    SellDetail target = new SellDetail(source.id);
    target.setUnits(source.units);
    target.setProduct(new Product(source.product.id));
    return target;
  }
}
