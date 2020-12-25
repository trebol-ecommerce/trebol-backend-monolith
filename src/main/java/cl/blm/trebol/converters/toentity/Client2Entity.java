package cl.blm.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.CustomerPojo;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.jpa.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Client2Entity
    implements Converter<CustomerPojo, Customer> {

  @Override
  public Customer convert(CustomerPojo source) {
    Customer target = new Customer(source.getId());
    target.setPerson(new Person(source.getPerson().getId()));
    return target;
  }
}
