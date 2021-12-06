package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.jpa.entities.Shipper;
import org.trebol.pojo.ShipperPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Shipper2Pojo
    implements Converter<Shipper, ShipperPojo> {

  @Override
  public ShipperPojo convert(Shipper source) {
    ShipperPojo target = new ShipperPojo();

    target.setId(source.getId());
    target.setName(source.getName());

    return target;
  }
}
