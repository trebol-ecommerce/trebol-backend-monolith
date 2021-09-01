package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SellPojo;
import org.trebol.jpa.entities.Sell;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Sell2Pojo
    implements Converter<Sell, SellPojo> {

  @Override
  public SellPojo convert(Sell source) {
    SellPojo target = new SellPojo();

    target.setId(source.getId());
    target.setDate(source.getDate());
    target.setNetValue(source.getNetValue());
    target.setToken(source.getTransactionToken());

    return target;
  }
}
