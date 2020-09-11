package cl.blm.newmarketing.store.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.api.pojo.SellTypePojo;
import cl.blm.newmarketing.store.jpa.entities.SellType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellType2Pojo
    implements Converter<SellType, SellTypePojo> {

  @Override
  public SellTypePojo convert(SellType source) {
    SellTypePojo target = new SellTypePojo();
    target.setId(source.getId());
    target.setName(source.getName());
    return target;
  }
}
