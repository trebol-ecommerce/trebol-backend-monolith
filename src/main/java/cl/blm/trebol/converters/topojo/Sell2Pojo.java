package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.jpa.entities.Sell;

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
    target.setSubtotal(source.getTotalValue());
    return target;
  }
}
