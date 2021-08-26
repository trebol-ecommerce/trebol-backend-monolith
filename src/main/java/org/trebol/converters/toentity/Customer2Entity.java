package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Customer2Entity
    implements Converter<CustomerPojo, Customer> {

  @Override
  public Customer convert(CustomerPojo source) {
    Customer target = new Customer();
    target.setId(source.getId());

    Person targetPerson = new Person();
    targetPerson.setId(source.getPerson().getId());
    target.setPerson(targetPerson);
    
    return target;
  }
}
