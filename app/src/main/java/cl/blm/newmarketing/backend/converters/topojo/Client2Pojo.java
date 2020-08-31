package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.model.entities.Client;

@Component
public class Client2Pojo
    implements Converter<Client, ClientPojo> {

  @Override
  public ClientPojo convert(Client source) {
    ClientPojo target = new ClientPojo();
    target.id = source.getId();
    target.person = new PersonPojo();
    target.person.id = source.getPerson().getId();
    return target;
  }
}
