package cl.blm.newmarketing.backend.converters.dto2pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.SellTypePojo;
import cl.blm.newmarketing.backend.dtos.SellTypeDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellTypeDto2Pojo
    implements Converter<SellTypeDto, SellTypePojo> {
  @Override
  public SellTypePojo convert(SellTypeDto source) {
    SellTypePojo target = new SellTypePojo();
    target.id = source.getSellTypeId();
    target.name = source.getSellTypeName();
    return target;
  }
}
