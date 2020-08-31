package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.model.entities.SellType;

@Component
public class SellType2Entity
    implements Converter<SellTypePojo, SellType> {

  @Override
  public SellType convert(SellTypePojo source) {
    SellType target = new SellType(source.id);
    target.setName(source.name);
    return target;
  }
}
