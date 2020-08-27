package cl.blm.newmarketing.backend.converters.dto2entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ClientDto;
import cl.blm.newmarketing.backend.model.entities.Client;
import cl.blm.newmarketing.backend.model.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ClientDto2Entity
    implements Converter<ClientDto, Client> {

  @Override
  public Client convert(ClientDto source) {
    Client target = new Client();
    target.setId(source.getClientId());

    if (source.getPerson() != null) {
      Person person = (new PersonDto2Entity()).convert(source.getPerson());
      target.setPerson(person);
    }

    return target;
  }
}
