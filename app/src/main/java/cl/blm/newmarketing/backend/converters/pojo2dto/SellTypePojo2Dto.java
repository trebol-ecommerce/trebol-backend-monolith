package cl.blm.newmarketing.backend.converters.pojo2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.SellTypePojo;
import cl.blm.newmarketing.backend.dtos.SellTypeDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellTypePojo2Dto
    implements Converter<SellTypePojo, SellTypeDto> {
  @Override
  public SellTypeDto convert(SellTypePojo source) {
    SellTypeDto target = new SellTypeDto();
    target.setSellTypeId(source.id);
    target.setSellTypeName(source.name);
    return target;
  }
}
