package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingCompanyPojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class BillingCompaniesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> {

  private final ConversionService conversion;

  @Autowired
  public BillingCompaniesConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public BillingCompanyPojo convertToPojo(BillingCompany source) {
    return conversion.convert(source, BillingCompanyPojo.class);
  }

  @Override
  public BillingCompany convertToNewEntity(BillingCompanyPojo source) {
    BillingCompany target = new BillingCompany();
    target.setIdNumber(source.getIdNumber());
    target.setName(source.getName());
    return target;
  }

  @Override
  public BillingCompany applyChangesToExistingEntity(BillingCompanyPojo source, BillingCompany existing)
          throws BadInputException {
    BillingCompany target = new BillingCompany(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}
