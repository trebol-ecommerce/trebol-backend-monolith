package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.jpa.entities.Customer;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Client2Pojo
    implements Converter<Customer, ClientPojo> {

  @Override
  public ClientPojo convert(Customer source) {
    ClientPojo target = new ClientPojo();
    target.setId(source.getId());
    return target;
  }
}
