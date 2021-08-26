package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SellTypePojo;
import org.trebol.jpa.entities.SellType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellType2Entity
    implements Converter<SellTypePojo, SellType> {

  @Override
  public SellType convert(SellTypePojo source) {
    SellType target = new SellType();
    target.setId(source.getId());
    target.setName(source.getName());
    return target;
  }
}
