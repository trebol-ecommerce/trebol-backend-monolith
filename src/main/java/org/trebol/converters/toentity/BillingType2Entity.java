package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.BillingTypePojo;
import org.trebol.jpa.entities.BillingType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class BillingType2Entity
    implements Converter<BillingTypePojo, BillingType> {

  @Override
  public BillingType convert(BillingTypePojo source) {
    BillingType target = new BillingType();
    target.setName(source.getName());
    return target;
  }
}
