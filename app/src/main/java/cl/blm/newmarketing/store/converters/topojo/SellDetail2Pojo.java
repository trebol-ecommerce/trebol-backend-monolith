package cl.blm.newmarketing.store.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.store.jpa.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetail2Pojo
    implements Converter<SellDetail, SellDetailPojo> {

  @Override
  public SellDetailPojo convert(SellDetail source) {
    SellDetailPojo target = new SellDetailPojo();
    target.setId(source.getId());
    target.setUnits(source.getUnits());
    return target;
  }
}
