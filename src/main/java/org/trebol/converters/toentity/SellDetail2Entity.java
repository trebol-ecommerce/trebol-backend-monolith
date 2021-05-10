package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.SellDetail;

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
