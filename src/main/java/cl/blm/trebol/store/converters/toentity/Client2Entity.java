package cl.blm.trebol.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.store.api.pojo.ClientPojo;
import cl.blm.trebol.store.jpa.entities.Client;
import cl.blm.trebol.store.jpa.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Client2Entity
    implements Converter<ClientPojo, Client> {

  @Override
  public Client convert(ClientPojo source) {
    Client target = new Client(source.getId());
    target.setPerson(new Person(source.getPerson().getId()));
    return target;
  }
}
