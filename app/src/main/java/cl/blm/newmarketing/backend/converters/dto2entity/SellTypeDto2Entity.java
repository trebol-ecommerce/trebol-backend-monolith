package cl.blm.newmarketing.backend.converters.dto2entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.model.entities.SellType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellTypeDto2Entity
    implements Converter<SellTypeDto, SellType> {
  @Override
  public SellType convert(SellTypeDto source) {
    SellType target = new SellType();
    target.setId(source.getSellTypeId());
    target.setName(source.getSellTypeName());
    return target;
  }
}
