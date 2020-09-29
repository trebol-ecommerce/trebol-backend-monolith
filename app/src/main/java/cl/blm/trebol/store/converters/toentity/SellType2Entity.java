package cl.blm.trebol.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.store.api.pojo.SellTypePojo;
import cl.blm.trebol.store.jpa.entities.SellType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellType2Entity
    implements Converter<SellTypePojo, SellType> {

  @Override
  public SellType convert(SellTypePojo source) {
    SellType target = new SellType(source.getId());
    target.setName(source.getName());
    return target;
  }
}
