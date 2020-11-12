package cl.blm.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.jpa.entities.Client;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Client2Pojo
    implements Converter<Client, ClientPojo> {

  @Override
  public ClientPojo convert(Client source) {
    ClientPojo target = new ClientPojo();
    target.setId(source.getId());
    return target;
  }
}
