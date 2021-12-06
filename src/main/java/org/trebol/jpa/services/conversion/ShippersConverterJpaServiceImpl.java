package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ShipperPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ShippersConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ShipperPojo, Shipper> {

  private final ConversionService conversion;

  @Autowired
  public ShippersConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public ShipperPojo convertToPojo(Shipper source) {
    return conversion.convert(source, ShipperPojo.class);
  }

  @Override
  public Shipper convertToNewEntity(ShipperPojo source) {
    Shipper target = new Shipper();
    target.setName(source.getName());
    return target;
  }

  @Override
  public Shipper applyChangesToExistingEntity(ShipperPojo source, Shipper existing) throws BadInputException {
    Shipper target = new Shipper(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}
