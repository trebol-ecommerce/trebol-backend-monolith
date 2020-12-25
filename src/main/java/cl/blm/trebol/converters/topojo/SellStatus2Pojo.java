package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellStatusPojo;
import cl.blm.trebol.jpa.entities.SellStatus;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellStatus2Pojo
    implements Converter<SellStatus, SellStatusPojo> {

  @Override
  public SellStatusPojo convert(SellStatus source) {
    SellStatusPojo target = new SellStatusPojo();
    target.setId(source.getId());
    target.setName(source.getName());
    return target;
  }
}
