package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.jpa.entities.Customer;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Customer2Pojo
    implements Converter<Customer, CustomerPojo> {

  @Override
  public CustomerPojo convert(Customer source) {
    CustomerPojo target = new CustomerPojo();
    target.setId(source.getId());
    return target;
  }
}
