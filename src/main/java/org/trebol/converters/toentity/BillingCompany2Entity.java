package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.api.pojo.BillingCompanyPojo;
import org.trebol.jpa.entities.BillingCompany;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class BillingCompany2Entity
    implements Converter<BillingCompanyPojo, BillingCompany> {

  @Override
  public BillingCompany convert(BillingCompanyPojo source) {
    BillingCompany target = new BillingCompany();
    target.setIdNumber(source.getIdNumber());
    target.setName(source.getName());
    return target;
  }
}
