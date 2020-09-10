package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.jpa.entities.SellType;

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
