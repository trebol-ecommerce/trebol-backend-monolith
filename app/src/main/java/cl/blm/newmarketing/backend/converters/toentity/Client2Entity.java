package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.Person;

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
