package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.pojo.BillingTypePojo;
import org.trebol.jpa.entities.BillingType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class BillingType2Pojo
    implements Converter<BillingType, BillingTypePojo> {

  @Override
  public BillingTypePojo convert(BillingType source) {
    BillingTypePojo target = new BillingTypePojo();

    target.setName(source.getName());

    return target;
  }
}
