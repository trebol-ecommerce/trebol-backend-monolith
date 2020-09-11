package cl.blm.newmarketing.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.store.jpa.entities.Product;
import cl.blm.newmarketing.store.jpa.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
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
