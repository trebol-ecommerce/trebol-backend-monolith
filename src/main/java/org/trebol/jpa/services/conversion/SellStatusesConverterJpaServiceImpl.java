package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SellStatusPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class SellStatusesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<SellStatusPojo, SellStatus> {

  private final ConversionService conversion;

  @Autowired
  public SellStatusesConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public SellStatusPojo convertToPojo(SellStatus source) {
    return conversion.convert(source, SellStatusPojo.class);
  }

  @Override
  public SellStatus convertToNewEntity(SellStatusPojo source) {
    SellStatus target = new SellStatus();
    target.setCode(source.getCode());
    target.setName(source.getName());
    return target;
  }

  @Override
  public SellStatus applyChangesToExistingEntity(SellStatusPojo source, SellStatus existing) throws BadInputException {
    SellStatus target = new SellStatus(existing);

    Integer code = source.getCode();
    if (code != null && !target.getCode().equals(code))  {
      target.setCode(code);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}
