package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.jpa.entities.SellType;

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
