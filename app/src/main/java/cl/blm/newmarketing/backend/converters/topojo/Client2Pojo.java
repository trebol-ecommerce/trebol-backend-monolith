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
    target.setId(source.getId());
    target.setPerson(new PersonPojo());
    target.getPerson().setId(source.getPerson().getId());
    target.getPerson().setName(source.getPerson().getName());
    return target;
  }
}
