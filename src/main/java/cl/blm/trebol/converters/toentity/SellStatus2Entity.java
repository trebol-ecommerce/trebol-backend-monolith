package cl.blm.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.SellStatusPojo;
import cl.blm.trebol.jpa.entities.SellStatus;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellStatus2Entity
    implements Converter<SellStatusPojo, SellStatus> {

  @Override
  public SellStatus convert(SellStatusPojo source) {
    SellStatus target = new SellStatus(source.getId());
    target.setName(source.getName());
    return target;
  }
}
