package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.jpa.entities.Product;
import cl.blm.newmarketing.backend.jpa.entities.SellDetail;

@Component
public class SellDetail2Entity
    implements Converter<SellDetailPojo, SellDetail> {

  @Override
  public SellDetail convert(SellDetailPojo source) {
    SellDetail target = new SellDetail(source.getId());
    target.setUnits(source.getUnits());
    target.setProduct(new Product(source.getProduct().getId()));
    return target;
  }
}
