package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.BillingCompanyPojo;
import org.trebol.jpa.entities.BillingCompany;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class BillingCompany2Pojo
    implements Converter<BillingCompany, BillingCompanyPojo> {

  @Override
  public BillingCompanyPojo convert(BillingCompany source) {
    BillingCompanyPojo target = new BillingCompanyPojo();

    target.setIdNumber(source.getIdNumber());
    target.setName(source.getName());

    return target;
  }
}
