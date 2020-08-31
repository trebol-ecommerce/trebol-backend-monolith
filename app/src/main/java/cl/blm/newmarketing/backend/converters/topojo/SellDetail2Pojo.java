package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.model.entities.SellDetail;

@Component
public class SellDetail2Pojo
    implements Converter<SellDetail, SellDetailPojo> {

  @Override
  public SellDetailPojo convert(SellDetail source) {
    SellDetailPojo target = new SellDetailPojo();
    target.id = source.getId();
    target.units = source.getUnits();
    target.product = new ProductPojo();
    target.product.id = source.getProduct().getId();
    return target;
  }
}
