package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingTypePojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class BillingTypesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<BillingTypePojo, BillingType> {

  private final ConversionService conversion;

  @Autowired
  public BillingTypesConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public BillingTypePojo convertToPojo(BillingType source) {
    return conversion.convert(source, BillingTypePojo.class);
  }

  @Override
  public BillingType convertToNewEntity(BillingTypePojo source) {
    BillingType target = new BillingType();
    target.setName(source.getName());
    return target;
  }

  @Override
  public BillingType applyChangesToExistingEntity(BillingTypePojo source, BillingType existing) throws BadInputException {
    BillingType target = new BillingType(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}
