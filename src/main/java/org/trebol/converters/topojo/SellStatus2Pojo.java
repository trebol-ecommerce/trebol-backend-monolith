package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.SellStatusPojo;
import org.trebol.jpa.entities.SellStatus;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellStatus2Pojo
    implements Converter<SellStatus, SellStatusPojo> {

  @Override
  public SellStatusPojo convert(SellStatus source) {
    SellStatusPojo target = new SellStatusPojo();
    target.setCode(source.getCode());
    target.setName(source.getName());
    return target;
  }
}
