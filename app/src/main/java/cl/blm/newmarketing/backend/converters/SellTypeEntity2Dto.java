package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.model.entities.SellType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellTypeEntity2Dto
    implements Converter<SellType, SellTypeDto> {
  @Override
  public SellTypeDto convert(SellType source) {
    SellTypeDto target = new SellTypeDto();
    target.setSellTypeId(source.getId());
    target.setSellTypeName(source.getName());
    return target;
  }
}
