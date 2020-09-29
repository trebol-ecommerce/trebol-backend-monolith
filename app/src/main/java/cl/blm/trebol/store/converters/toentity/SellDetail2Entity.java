package cl.blm.trebol.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.store.api.pojo.SellDetailPojo;
import cl.blm.trebol.store.jpa.entities.Product;
import cl.blm.trebol.store.jpa.entities.SellDetail;

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
